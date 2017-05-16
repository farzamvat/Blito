package blito.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.HostType;
import com.blito.enums.ImageType;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.eventhost.EventHostSimpleViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.EventHostService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class EventHostServiceTest {

	@Autowired
	private EventHostRepository hostRepo;
	@Autowired
	private ImageRepository imageRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private EventHostService hostService;
	
	private boolean isInit = true;
	private User user = new User();
	private EventHostViewModel createVmodel = new EventHostViewModel();
	private EventHostViewModel updateVmodel = new EventHostViewModel();
	private EventHostViewModel updateExceptionVmodel = new EventHostViewModel();
	private EventHostViewModel getVmodel = new EventHostViewModel();
	private EventHostViewModel deleteVmodel = new EventHostViewModel();
	private EventHostViewModel vmodel1 = new EventHostViewModel();
	private EventHostViewModel vmodel2 = new EventHostViewModel();
	private EventHostViewModel vmodel3 = new EventHostViewModel();
	private EventHostViewModel vmodel4 = new EventHostViewModel();

	@Before
	public void init() {
		if (isInit) {
			user.setFirstname("hasti");
			user.setEmail("hasti.sahabi@gmail.com");
			user = userRepo.save(user);
			user.setActive(true);
			SecurityContextHolder.setCurrentUser(user);
			Image image1 = new Image();
			image1.setImageUUID(Constants.DEFAULT_HOST_PHOTO);
			image1.setImageType(ImageType.HOST_PHOTO);
			imageRepo.save(image1);
			Image image2 = new Image();
			image2.setImageType(ImageType.HOST_COVER_PHOTO);
			image2.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO);
			imageRepo.save(image2);

			createVmodel.setHostName("Shenakht");
			createVmodel.setTelephone("22431103");
			createVmodel.setHostType(HostType.COFFEESHOP);

			updateVmodel.setHostName("Lucky Clover Cafe");
			updateVmodel.setTelephone("22431103");
			updateVmodel.setHostType(HostType.COFFEESHOP);
			

			updateExceptionVmodel.setHostName("Roo Be Roo");
			updateExceptionVmodel.setTelephone("22411254");
			updateExceptionVmodel.setHostType(HostType.COFFEESHOP);
			
			getVmodel.setHostName("Wispo");
			getVmodel.setTelephone("22412345");
			getVmodel.setHostType(HostType.COFFEESHOP);
			
			deleteVmodel.setHostName("Azadi");
			deleteVmodel.setTelephone("22412345");
			deleteVmodel.setHostType(HostType.CULTURALCENTER);
			
			isInit = false;
		}
	}

	@Test
	public void create() {
		createVmodel = hostService.create(createVmodel);
		assertNotNull(hostRepo.findOne(createVmodel.getEventHostId()));
		assertEquals(SecurityContextHolder.currentUser().getUserId(),
				hostRepo.findOne(createVmodel.getEventHostId()).getUser().getUserId());
		assertNotNull(userRepo.findOne(SecurityContextHolder.currentUser().getUserId()).getEventHosts().stream()
				.filter(e -> e.getEventHostId() == createVmodel.getEventHostId()).findFirst().get());
		assertNotEquals(createVmodel.getImages().size(), 0);
	}

	@Test
	public void update() {
		updateVmodel = hostService.create(updateVmodel);
		updateVmodel.setTelephone("22545079");
		updateVmodel = hostService.update(updateVmodel);
		assertEquals("22545079", updateVmodel.getTelephone());
		assertNotNull(userRepo.findOne(SecurityContextHolder.currentUser().getUserId()).getEventHosts().stream()
				.filter(e -> e.getEventHostId() == updateVmodel.getEventHostId()).findFirst().get());
		assertNotEquals(updateVmodel.getImages().size(), 0);
	}
	
	@Test(expected = NotAllowedException.class)
	public void updateWithException() {
		updateExceptionVmodel = hostService.create(updateExceptionVmodel);
		User newUser = new User();
		newUser.setFirstname("farzam");
		newUser.setEmail("farzam.vat@gmail.com");
		newUser.setActive(true);
		newUser = userRepo.save(newUser);
		SecurityContextHolder.setCurrentUser(newUser);
		updateExceptionVmodel = hostService.update(updateExceptionVmodel);
	}
	
	@Test
	public void get() {
		getVmodel = hostService.create(getVmodel);
		EventHostViewModel foundEventHost = hostService.get(getVmodel.getEventHostId());
		assertEquals(getVmodel.getEventHostId(), foundEventHost.getEventHostId());
	}
	
	@Test(expected = NotFoundException.class)
	public void getWithException() {
		getVmodel  = hostService.get(45345);
	}
	
	@Test 
	public void delete() {
		deleteVmodel = hostService.create(deleteVmodel);
		assertNotNull(hostRepo.findOne(deleteVmodel.getEventHostId()));
		hostService.delete(deleteVmodel.getEventHostId());
		assertNull(hostRepo.findOne(deleteVmodel.getEventHostId()));
	}
	
	@Test
	public void currentUserEventHosts() {
		SecurityContextHolder.currentUser().getEventHosts().stream().forEach(e->hostService.delete(e.getEventHostId()));
		assertEquals(0, SecurityContextHolder.currentUser().getEventHosts().size());
		vmodel1.setHostName("Shenakht");
		vmodel1.setTelephone("22431103");
		vmodel1.setHostType(HostType.COFFEESHOP);

		vmodel2.setHostName("Lucky Clover Cafe");
		vmodel2.setTelephone("22431103");
		vmodel2.setHostType(HostType.COFFEESHOP);
		

		vmodel3.setHostName("Roo Be Roo");
		vmodel3.setTelephone("22411254");
		vmodel3.setHostType(HostType.COFFEESHOP);
		
		vmodel4.setHostName("Wispo");
		vmodel4.setTelephone("22412345");
		vmodel4.setHostType(HostType.COFFEESHOP);
		
		vmodel1 = hostService.create(vmodel1);
		vmodel2 = hostService.create(vmodel2);
		vmodel3 = hostService.create(vmodel3);
		vmodel4 = hostService.create(vmodel4);
		
		assertEquals(4, SecurityContextHolder.currentUser().getEventHosts().size());
		
		List<EventHostSimpleViewModel> hosts = hostService.getCurrentUserEventHosts();
		
		assertEquals(4, hosts.size());

	}

}
