package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

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
import com.blito.mappers.ImageMapper;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.EventHostRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
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
	@Autowired
	private ImageMapper imageMapper;
	private boolean isInit = true;
	private User user = new User();
	private EventHostViewModel createvmodel = new EventHostViewModel();
	private EventHostViewModel updatevmodel = new EventHostViewModel();

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

			createvmodel.setHostName("Shenakht");
			createvmodel.setTelephone("22431103");
			createvmodel.setHostType(HostType.COFFEESHOP);
			createvmodel.setImages(
					Arrays.asList(imageMapper.createFromEntity(image1), imageMapper.createFromEntity(image2)));

			updatevmodel.setHostName("Lucky Clover Cafe");
			updatevmodel.setTelephone("22431103");
			updatevmodel.setHostType(HostType.COFFEESHOP);
			updatevmodel.setImages(
					Arrays.asList(imageMapper.createFromEntity(image1), imageMapper.createFromEntity(image2)));
			isInit = false;
		}
	}

	
	@Test
	public void create() {
		createvmodel = hostService.create(createvmodel);
		assertEquals(1, hostRepo.count());
		assertEquals(SecurityContextHolder.currentUser().getUserId(),
				hostRepo.findOne(createvmodel.getEventHostId()).getUser().getUserId());
		assertEquals(userRepo.findOne(SecurityContextHolder.currentUser().getUserId()).getEventHosts().size(), 1);
	}

//	@Test
//	public void update() {
//		updatevmodel = hostService.create(updatevmodel);
//		updatevmodel.setTelephone("22545079");
//		updatevmodel = hostService.update(updatevmodel);
//		assertEquals("22545079", updatevmodel.getTelephone());
//
//	}

}
