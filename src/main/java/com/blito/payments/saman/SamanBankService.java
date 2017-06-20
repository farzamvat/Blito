package com.blito.payments.saman;

import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.blito.exceptions.SamanBankException;
import com.blito.payments.saman.webservice.client.InitPaymentSoap;
import com.blito.payments.saman.webservice.client.InitPaymentSoapProxy;
import com.blito.payments.saman.webservice.client.ReferencePaymentSoap;
import com.blito.payments.saman.webservice.client.ReferencePaymentSoapProxy;

@Service
public class SamanBankService {
	@Value("${saman.bank.merchantCode}")
	private String merchantCode;
	@Value("${saman.bank.password}")
	private String samanPassword;
	private InitPaymentSoap _initPaymentSoap;
	private ReferencePaymentSoap _referencePaymentSoap;

	private final Logger log = LoggerFactory.getLogger(SamanBankService.class);

	@PostConstruct
	public void _init() {
		if (_initPaymentSoap == null)
			_initPaymentSoap = new InitPaymentSoapProxy();
		if(_referencePaymentSoap == null)
			_referencePaymentSoap = new ReferencePaymentSoapProxy();
	}

	public CompletableFuture<String> requestToken(String reservationNumber, long totalAmount) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				log.debug("Calling requestToken method with reservationNumber '{}' and totalAmount '{}'",
						reservationNumber, totalAmount);
				return _initPaymentSoap.requestToken(merchantCode, reservationNumber, totalAmount, 0, 0, 0, 0, 0, 0, "",
						"", 0);
			} catch (RemoteException e) {
				log.error("Exception in sending requestToken Saman bank '{}'", e.getMessage());
				throw new SamanBankException(e.getMessage());
			}
		}).thenApply(this::responseStatusChecker);
	}

	public String responseStatusChecker(String result) {
		if (IntStream.range(-18, 0).filter(i -> (i != -2) || (i != -5)).mapToObj(String::valueOf).filter(result::equals)
				.findFirst().isPresent()) {
			log.error("Error in result of requestToken Saman bank '{}'", result);
			throw new SamanBankException(SamanBankException.getError(result));
		}
		log.debug("Saman bank requestToken result '{}'", result);
		return result;
	}
	
	public String reverseTransactionResponseStatusChecker(String result) {
		if(result.equals("-1"))
		{
			throw new SamanBankException("Error in reverseTransaction");
		}
		return result;
	}
	
	public CompletableFuture<String> revereseTransaction(String refNumber)
	{
		return CompletableFuture.supplyAsync(() -> {
			try {
				log.debug("Calling reverseTransaction with referenceNumber '{}' and MerchantId '{}'",refNumber,merchantCode);
				return String.valueOf(_referencePaymentSoap.reverseTransaction(refNumber, merchantCode, merchantCode, samanPassword));
			} catch (RemoteException e) {
				log.error("Exception in sending reverseTransaction Saman bank '{}'",e.getMessage()); 
				throw new SamanBankException(e.getMessage());
			}
		}).thenApply(this::reverseTransactionResponseStatusChecker);
	}

	public CompletableFuture<String> verifyTransaction(String refNumber)
	{
		return CompletableFuture.supplyAsync(() -> {
			try {
				log.debug("Calling verifyTransaction method with referenceNumber '{}' and MerchantId '{}'",refNumber,merchantCode);
				return String.valueOf(_referencePaymentSoap.verifyTransaction(refNumber, merchantCode));
			} catch (RemoteException e) {
				log.error("Exception in sending verifyTransaction Saman bank '{}'", e.getMessage());
				throw new SamanBankException(e.getMessage());
			}
		}).thenApply(this::responseStatusChecker);
	}
}
