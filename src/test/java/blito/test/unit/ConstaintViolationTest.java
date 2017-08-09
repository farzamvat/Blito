package blito.test.unit;


import static org.junit.Assert.*;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.models.User;
import com.blito.repositories.UserRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class ConstaintViolationTest {

	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void exceptionTest()
	{
		User user = new User();
		user.setEmail("farzam.vat@gmail.com");
		user.setPassword("12345678");
		userRepository.save(user);
		
		try {

			User user1 = new User();
			user1.setEmail("farzam.vat@gmail.com");
			user1.setPassword("12345678");
			userRepository.save(user1);
			assertTrue(false);
		}
		catch(Exception e)
		{
			System.err.println(e);
			assertTrue(true);
		}
	}
}
