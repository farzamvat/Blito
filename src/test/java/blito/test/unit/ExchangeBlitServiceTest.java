package blito.test.unit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.ImageType;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.ExchangeBlitService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ExchangeBlitServiceTest {
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private ExchangeBlitRepository exRepo;
	@Autowired
	private ExchangeBlitService exService;
	@Autowired
	private ImageRepository imageRepository; 
	private static boolean isInit = true;
	User user = new User();
	ExchangeBlitViewModel vmodel = new ExchangeBlitViewModel();
	ExchangeBlitViewModel vmodel2 = new ExchangeBlitViewModel();
	
	@Before
	public void init()
	{
		if(isInit)
		{
			Image image = new Image();
			image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);
			image.setImageType(ImageType.EXCHANGEBLIT_PHOTO);
			imageRepository.save(image);
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user = userRepository.save(user);
			SecurityContextHolder.setCurrentUser(user);
			vmodel.setBlitCost(200.2);
			vmodel.setDescription("asdsad");
			vmodel.setEmail("farzam.vat@gmail.com");
			vmodel2.setBlitCost(500.5);
			vmodel2.setDescription("sfgdhfhfjfjghj");
			vmodel2.setEmail("farzam.vat@gmail.com");
			isInit = false;

		}
	}
	
//	@Test
//	public void create()
//	{
//		vmodel = exService.create(vmodel);
//		vmodel2 = exService.create(vmodel2);
//		assertEquals(2, exRepo.count());
//	}
//	
//	@Test
//	public void findById()
//	{
//		vmodel = exService.create(vmodel);
//		ExchangeBlitViewModel foundExBlit = exService.getExchangeBlitById(vmodel.getExchangeBlitId());
//		assertEquals(vmodel.getExchangeBlitId(), foundExBlit.getExchangeBlitId());
//	}
//	
//	@Test
//	public void update() {
//		vmodel2 = exService.create(vmodel2);
//		
//		ExchangeBlitViewModel newVmodel = new ExchangeBlitViewModel();
//		newVmodel.setExchangeBlitId(vmodel2.getExchangeBlitId());
//		newVmodel.setBlitCost(432.345);
//		newVmodel.setDescription("sfgdhfhfjfjghj");
//		newVmodel.setEmail("farzam.vat@gmail.com");
//		
//		newVmodel = exService.update(newVmodel);
//		assertEquals("sfgdhfhfjfjghj", newVmodel.getDescription());
//	}
//	
	@Test
	public void delete() {
		vmodel = exService.create(vmodel);
		assertEquals(1, exRepo.count());
		exService.delete(vmodel.getExchangeBlitId());
		assertEquals(0, exRepo.count());
	}

}
