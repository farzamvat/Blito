package blito.test.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.exceptions.NotFoundException;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.account.UserViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminAccountService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AdminAccountServiceTest {
	
	@Autowired
	UserRepository userRepo;
	@Autowired
	AdminAccountService adminAccService;
	
	private boolean isInit = false;
	private User admin = new User();
	private User user1 = new User();
	private User user2 = new User();
	private User user3 = new User();
	private User user4 = new User();
	
	@Before
	public void init() {
		if(!isInit) {
			admin.setFirstname("Admin");
			admin.setEmail("admin@gamil.com");
			admin.setActive(true);
			SecurityContextHolder.setCurrentUser(userRepo.save(admin));
			
			user1.setFirstname("Hasti");
			user1.setEmail("hasti.sahabi@gamil.com");
			user1.setActive(true);
			user1 = userRepo.save(user1);
			
			user2.setFirstname("Farzam");
			user2.setEmail("farzam@gamil.com");
			user2.setActive(true);
			user2 = userRepo.save(user2);
			
			user3.setFirstname("Soroush");
			user3.setEmail("soroush@gamil.com");
			user3.setActive(true);
			user3 = userRepo.save(user3);
			
			user4.setFirstname("Kimi");
			user4.setEmail("kimi@gamil.com");
			user4.setActive(true);
			user4 = userRepo.save(user4);
			
			isInit = true;
		}
		
	}
	
	@Test
	public void get() {
		UserViewModel foundUser = adminAccService.getUser(user4.getUserId());
		assertEquals(user4.getEmail(), foundUser.getEmail());
	}
	
	@Test(expected = NotFoundException.class)
	public void getWithNotFoundException() {
		UserViewModel foundUser = adminAccService.getUser(2342);
	}
	
	@Test
	public void getAll() {
		Pageable pageable = new PageRequest(0,10);
		Page<UserViewModel> users = adminAccService.getAllUsers(pageable);
		assertEquals(5, users.getNumberOfElements());
	}
	
	@Test
	public void ban() {
		adminAccService.banUser(user1.getUserId());
		user1 = userRepo.findOne(user1.getUserId());
		assertEquals(true, user1.isBanned());
		assertEquals("Hasti", user1.getFirstname());
		adminAccService.unBanUser(user1.getUserId());
		user1 = userRepo.findOne(user1.getUserId());
		assertEquals(false, user1.isBanned());
	}
	

}
