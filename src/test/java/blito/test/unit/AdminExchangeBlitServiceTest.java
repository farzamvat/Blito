package blito.test.unit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.UserRepository;
import com.blito.services.AdminExchangeBlitService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AdminExchangeBlitServiceTest {
	
	@Autowired
	ExchangeBlitRepository exRepo;
	@Autowired
	AdminExchangeBlitService admiExService;
	@Autowired
	UserRepository userRepo;
	
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void getallPageable() {
		
	}
	

}
