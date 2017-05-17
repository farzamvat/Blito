package blito.test.unit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.models.User;
import com.blito.repositories.EventRepository;
import com.blito.repositories.UserRepository;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminEventService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AdminEventServiceTest {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	EventRepository eventRepo;
	@Autowired
	AdminEventService adminEventService;
	
	private boolean isInit = false;
	private User user = new User();
	
	@Before
	public void init() {
		if(!isInit) {
			user.setFirstname("hasti");
			user.setEmail("hasti.sahabi@gmail.com");
			user.setActive(true);
			SecurityContextHolder.setCurrentUser(userRepo.save(user));
			
			
			isInit = true;
		}
	}
}
