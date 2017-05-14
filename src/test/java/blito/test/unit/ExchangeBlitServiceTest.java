package blito.test.unit;

import static org.junit.Assert.assertEquals;

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
	private static boolean isInit = false;
	User user = new User();
	ExchangeBlitViewModel vmodel = new ExchangeBlitViewModel();
	@Before
	public void init()
	{
		if(!isInit)
		{
			Image image = new Image();
			image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);
			image.setImageType(ImageType.EXCHANGEBLIT_PHOTO);
			imageRepository.save(image);
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user = userRepository.save(user);
			SecurityContextHolder.setCurrentUser(user);
			isInit = true;
			vmodel.setBlitCost(200.2);
			vmodel.setDescription("asdsad");
			vmodel.setEmail("farzam.vat@gmail.com");
			vmodel.setFirstname("farzam");
		}
	}
	
	@Test
	public void create()
	{
		ExchangeBlitViewModel viewModel = exService.create(vmodel);
		assertEquals(1, exRepo.count());
	}
}
