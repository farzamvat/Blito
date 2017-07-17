package blito.test.unit;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.payments.saman.webservice.client.InitPaymentSoap;
import com.blito.payments.saman.webservice.client.InitPaymentSoapProxy;
import com.blito.services.RandomUtil;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class SamanBankTest {
	@Before
	public void init()
	{
		
	}
	
	@Test
	public void test()
	{
		InitPaymentSoap payment = new InitPaymentSoapProxy();
		
		String result="";
		try {
			result = payment.requestToken("10797129", "1", 1000, 0, 0, 0, 0, 0, 0, "", "", 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(result);
		
		System.out.println(RandomUtil.generateTrackCode());
	}
}
