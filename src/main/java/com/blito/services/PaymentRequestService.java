package com.blito.services;

import com.blito.enums.BankGateway;
import com.blito.enums.Response;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.PayDotIrException;
import com.blito.mappers.CommonBlitMapper;
import com.blito.models.Blit;
import com.blito.payments.payir.viewmodel.PayDotIrClient;
import com.blito.payments.saman.SamanBankService;
import com.blito.payments.zarinpal.client.ZarinpalClient;
import com.blito.repositories.BlitTypeRepository;
import com.blito.repositories.CommonBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.payments.PayDotIrRequestViewModel;
import com.blito.rest.viewmodels.payments.PaymentRequestViewModel;
import com.blito.rest.viewmodels.payments.ZarinpalPayRequestResponseViewModel;
import com.blito.services.util.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentRequestService {
	@Autowired
	private SamanBankService samanBankService;
	@Autowired
	private ZarinpalClient zarinpalClient;
	@Autowired
	private BlitTypeRepository blitTypeRepository;
	@Autowired
	private MailService mailService;
	@Autowired
	private SmsService smsService;
	@Autowired
	private CommonBlitMapper commonBlitMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HtmlRenderer htmlRenderer;
	@Autowired
	private CommonBlitRepository commonBlitRepository;
	@Autowired
	private DiscountService discountService;
	@Autowired
	private PayDotIrClient payDotIrClient;
	
	@Value("${zarinpal.web.gateway}")
	private String zarinpalGatewayURL;
	@Value("${pay.ir.payment.gateway}")
	private String payDotIrGateway;

	private final Logger log = LoggerFactory.getLogger(PaymentRequestService.class);
	
	public CompletableFuture<String> samanBankRequestToken(String reservationNumber, long totalAmount) {
		return samanBankService.requestToken(reservationNumber, totalAmount);
	}

	public String zarinpalRequestToken(int amount, String email, String mobile, String description) {
		return zarinpalClient.getPaymentRequest(amount, email, mobile, description);
	}

	@Transactional
	public String createPurchaseRequest(Blit blit) {
		switch (Enum.valueOf(BankGateway.class, blit.getBankGateway())) {
			case ZARINPAL:
				log.debug("Before requesting token from zarinpal gateway user email '{}' and blit track code '{}'",
						blit.getCustomerEmail(), blit.getTrackCode());
				return zarinpalRequestToken(blit.getTotalAmount().intValue(), blit.getCustomerEmail(),
						blit.getCustomerMobileNumber(), blit.getEventName());

			case PAYDOTIR:
				log.debug("Before requesting token from zarinpal gateway user email '{}' and blit track code '{}'",
						blit.getCustomerEmail(), blit.getTrackCode());
				throw new PayDotIrException(ResourceUtil.getMessage(Response.PAY_DOT_IR_ERROR));
				// TODO: 12/2/17 zarinpal pay.ir tokens
//				return payDotIrClient.createPaymentRequest(blit.getTotalAmount().intValue(),
//						blit.getCustomerMobileNumber(),
//						blit.getTrackCode()).map(PayDotIrResponse::getTransId).map(String::valueOf)
//						.getOrElseThrow(() -> new PayDotIrException(ResourceUtil.getMessage(Response.PAY_DOT_IR_ERROR)));

		default:
			throw new NotFoundException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}
	}

	public PaymentRequestViewModel createPaymentRequest(BankGateway bankGateway,String token) {
		switch (bankGateway) {
			case ZARINPAL:
				return new ZarinpalPayRequestResponseViewModel(zarinpalGatewayURL + token);
			case PAYDOTIR:
				return new PayDotIrRequestViewModel(payDotIrGateway + token);
			default:
				throw new RuntimeException(ResourceUtil.getMessage(Response.BANK_GATEWAY_NOT_FOUND));
		}

	}
}
