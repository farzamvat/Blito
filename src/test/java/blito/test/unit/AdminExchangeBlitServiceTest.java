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
import com.blito.configs.Constants;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.mappers.ExchangeBlitMapper;
import com.blito.models.ExchangeBlit;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.exchangeblit.AdminChangeExchangeBlitStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.AdminExchangeBlitService;

//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class AdminExchangeBlitServiceTest {
	
	@Autowired
	ExchangeBlitRepository exRepo;
	@Autowired
	AdminExchangeBlitService adminExService;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ImageRepository imageRepo;
	@Autowired
	ExchangeBlitMapper exMapper;
	
	private User admin = new User();
	private User user = new User();
	
	private ExchangeBlit exBlit1 = new ExchangeBlit();
	private ExchangeBlit exBlit2 = new ExchangeBlit();
	private ExchangeBlit exBlit3 = new ExchangeBlit();
	private ExchangeBlit exBlit4 = new ExchangeBlit();
	
	private Image image = new Image();
	
	@Before
	public void init() {
		admin.setFirstname("Hasti");
		admin.setEmail("hasti@gmail.com");
		admin.setActive(true);
		SecurityContextHolder.setCurrentUser(userRepo.save(admin));
		
		user.setFirstname("Fifi");
		user.setEmail("fifi@gmail.com");
		user.setActive(true);
		
		image.setImageType(ImageType.EXCHANGEBLIT_PHOTO);
		image.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);
		image = imageRepo.save(image);
		
		exBlit1.setTitle("ExBlit1");
		exBlit1.setImage(image);
		exBlit1.setUser(user);
		exBlit1.setType(ExchangeBlitType.CONCERT);
		exBlit1.setOperatorState(OperatorState.PENDING);
		exBlit1.setState(State.CLOSED);
		exBlit1 = exRepo.save(exBlit1);
		user.getExchangeBlits().add(exBlit1);
		
		exBlit2.setTitle("ExBlit2");
		exBlit2.setImage(image);
		exBlit2.setUser(user);
		exBlit2.setType(ExchangeBlitType.CONCERT);
		exBlit2.setOperatorState(OperatorState.APPROVED);
		exBlit2.setState(State.OPEN);
		exBlit2 = exRepo.save(exBlit2);
		user.getExchangeBlits().add(exBlit2);
		
		exBlit3.setTitle("ExBlit3");
		exBlit3.setImage(image);
		exBlit3.setUser(user);
		exBlit3.setType(ExchangeBlitType.CONCERT);
		exBlit3.setOperatorState(OperatorState.APPROVED);
		exBlit3.setState(State.OPEN);
		exBlit3 = exRepo.save(exBlit3);
		user.getExchangeBlits().add(exBlit3);
		
		exBlit4.setTitle("ExBlit4");
		exBlit4.setImage(image);
		exBlit4.setUser(user);
		exBlit4.setType(ExchangeBlitType.CONCERT);
		exBlit4.setOperatorState(OperatorState.APPROVED);
		exBlit4.setState(State.OPEN);
		exBlit4 = exRepo.save(exBlit4);
		user.getExchangeBlits().add(exBlit4);
		
		user = userRepo.save(user);
		
	}
	
	@Test
	public void getallPageable() {
		Pageable pageable = new PageRequest(0,5);
		Page<ExchangeBlitViewModel> exBlits = adminExService.exchangeBlitsByPage(pageable);
		assertEquals(4, exBlits.getNumberOfElements());	
	}
	
	@Test
	public void changeState() {
		assertEquals(OperatorState.PENDING, exBlit1.getOperatorState());
		AdminChangeExchangeBlitStateViewModel vmodel = new AdminChangeExchangeBlitStateViewModel();
		vmodel.setExchangeBlitId(exBlit1.getExchangeBlitId());
		vmodel.setOperatorState(OperatorState.APPROVED);
		ExchangeBlitViewModel exBlit = adminExService.changeExchangeBlitState(vmodel);
		assertEquals(OperatorState.APPROVED, exBlit.getOperatorState());
		assertEquals(OperatorState.APPROVED, exRepo.findOne(exBlit1.getExchangeBlitId()).getOperatorState());
	}

}
