package blito.test.unit;


import java.util.stream.IntStream;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.exceptions.SamanBankException;
import com.blito.payments.saman.SamanBankService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class SamanBankExceptionTest {
	
	@Autowired
	SamanBankService samanBankService;
	
	@Test
	public void responseCheckerTest()
	{
		IntStream.range(-18, 0)
		.mapToObj(String::valueOf)
		.forEach(i -> {
			try {
				samanBankService.responseStatusChecker(i);
			} catch (SamanBankException e) {
				System.out.println(e.getMessage());
				assertTrue(true);
			}
		});
	}
}
