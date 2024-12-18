package blito.test.unit;

import com.blito.Application;
import com.blito.configs.Constants;
import com.blito.enums.ExchangeBlitType;
import com.blito.enums.ImageType;
import com.blito.enums.OperatorState;
import com.blito.enums.State;
import com.blito.models.Image;
import com.blito.models.User;
import com.blito.repositories.ExchangeBlitRepository;
import com.blito.repositories.ImageRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitChangeStateViewModel;
import com.blito.rest.viewmodels.exchangeblit.ExchangeBlitViewModel;
import com.blito.security.SecurityContextHolder;
import com.blito.services.ExchangeBlitService;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Transactional
public class ExchangeBlitServiceTest {
	@Autowired 
	private UserRepository userRepository;
	@Autowired
	private ExchangeBlitRepository exRepo;
	@Autowired
	private ExchangeBlitService exService;
	@Autowired
	private ImageRepository imageRepository; 
	User user = new User();
	User user2 = new User();
	ExchangeBlitViewModel createExBlitVmodel1 = new ExchangeBlitViewModel();
	ExchangeBlitViewModel createExBlitVmodel2 = new ExchangeBlitViewModel();
	ExchangeBlitViewModel updateExBlitVmodel = new ExchangeBlitViewModel();
	ExchangeBlitViewModel getExBlitVmodel = new ExchangeBlitViewModel();
	
	ExchangeBlitViewModel myExBlitVmodel1 = new ExchangeBlitViewModel();
	ExchangeBlitViewModel myExBlitVmodel2 = new ExchangeBlitViewModel();
	ExchangeBlitViewModel myExBlitVmodel3 = new ExchangeBlitViewModel();
	
	@Before
	public void init()
	{
			Image image = imageRepository.findByImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO).get();
			
			user.setEmail("farzam.vat@gmail.com");
			user.setActive(true);
			user = userRepository.save(user);
			SecurityContextHolder.setCurrentUser(user);
			
			user2.setEmail("hasti.sahabi@gmail.com");
			user2.setActive(true);
			user2 = userRepository.save(user2);
			
			createExBlitVmodel1.setBlitCost(200.2);
			createExBlitVmodel1.setTitle("ex1");
			createExBlitVmodel1.setDescription("estakhr");
			createExBlitVmodel1.setEmail("farzam.vat@gmail.com");
			createExBlitVmodel1.setType(ExchangeBlitType.CINEMA);
			createExBlitVmodel2.setBlitCost(500.5);
			createExBlitVmodel2.setTitle("ex2");
			createExBlitVmodel2.setDescription("theater");
			createExBlitVmodel2.setEmail("farzam.vat@gmail.com");
			createExBlitVmodel2.setType(ExchangeBlitType.CINEMA);
			updateExBlitVmodel.setBlitCost(1500);
			updateExBlitVmodel.setTitle("ex3");
			updateExBlitVmodel.setDescription("cinema");
			updateExBlitVmodel.setEmail("farzam.vat@gmail.com");
			updateExBlitVmodel.setType(ExchangeBlitType.CINEMA);
			getExBlitVmodel.setBlitCost(2000);
			getExBlitVmodel.setTitle("ex4");
			getExBlitVmodel.setType(ExchangeBlitType.CINEMA);
			getExBlitVmodel.setDescription("cafe");
			getExBlitVmodel.setEmail("farzam.vat@gmail.com");
			
			myExBlitVmodel1.setBlitCost(1);
			myExBlitVmodel1.setTitle("ex5");
			myExBlitVmodel1.setDescription("my1");
			myExBlitVmodel1.setEmail("hasti.sahabi@gmail.com");
			myExBlitVmodel1.setType(ExchangeBlitType.CINEMA);
			myExBlitVmodel2.setBlitCost(2);
			myExBlitVmodel2.setTitle("ex6");
			myExBlitVmodel2.setDescription("my2");
			myExBlitVmodel2.setEmail("hasti.sahabi@gmail.com");
			myExBlitVmodel2.setType(ExchangeBlitType.CINEMA);
			myExBlitVmodel3.setBlitCost(3);
			myExBlitVmodel3.setTitle("ex7");
			myExBlitVmodel3.setDescription("my3");
			myExBlitVmodel3.setEmail("hasti.sahabi@gmail.com");
			myExBlitVmodel3.setType(ExchangeBlitType.CINEMA);


	}
	
	@Test
	public void create()
	{
		createExBlitVmodel1 = exService.create(createExBlitVmodel1);
		createExBlitVmodel2 = exService.create(createExBlitVmodel2);
		assertEquals(ImageType.EXCHANGEBLIT_PHOTO, createExBlitVmodel1.getImage().getType());
		
		assertEquals(2, SecurityContextHolder.currentUser().getExchangeBlits().size());
		assertEquals(2, userRepository.findOne(SecurityContextHolder.currentUser().getUserId()).getExchangeBlits().size());
		assertEquals(2, exRepo.count());
	}
	
	@Test
	public void findById()
	{
		getExBlitVmodel = exService.create(getExBlitVmodel);
		ExchangeBlitViewModel foundExBlit = exService.getExchangeBlitByLink(getExBlitVmodel.getExchangeLink());
		assertEquals(getExBlitVmodel.getExchangeBlitId(), foundExBlit.getExchangeBlitId());
	}
	
	@Test
	public void update() {
		updateExBlitVmodel = exService.create(updateExBlitVmodel);
		exRepo.findOne(updateExBlitVmodel.getExchangeBlitId()).setState(State.OPEN.name());
		exRepo.findOne(updateExBlitVmodel.getExchangeBlitId()).setOperatorState(OperatorState.APPROVED.name());
		ExchangeBlitViewModel newVmodel = new ExchangeBlitViewModel();
		newVmodel.setExchangeBlitId(updateExBlitVmodel.getExchangeBlitId());
		newVmodel.setBlitCost(5000);
		newVmodel.setDescription("cafe updated");
		newVmodel.setEmail("farzam.vat@gmail.com");
		newVmodel = exService.update(newVmodel);
		assertEquals("cafe updated", newVmodel.getDescription());
		assertEquals(1, SecurityContextHolder.currentUser().getExchangeBlits().size());

	}
	
	@Test
	public void delete() {
		updateExBlitVmodel = exService.create(updateExBlitVmodel);
		assertEquals(1, exRepo.count());
		assertEquals(1, SecurityContextHolder.currentUser().getExchangeBlits().size());

		exService.delete(updateExBlitVmodel.getExchangeBlitId());
		assertEquals(1, exRepo.count());
		assertEquals(1, SecurityContextHolder.currentUser().getExchangeBlits().size());
	}
	
	@Test
	public void currentUserExchangeBlitsTest() {
		createExBlitVmodel1 = exService.create(createExBlitVmodel1);
		createExBlitVmodel2 = exService.create(createExBlitVmodel2);
		SecurityContextHolder.setCurrentUser(user2);
		myExBlitVmodel1 = exService.create(myExBlitVmodel1);
		myExBlitVmodel2 = exService.create(myExBlitVmodel2);
		myExBlitVmodel3 = exService.create(myExBlitVmodel3);
		assertEquals(5, exRepo.count());
		Pageable pageable = new PageRequest(0,5);
		Page<ExchangeBlitViewModel> exBlits = exService.currentUserExchangeBlits(pageable);
		assertEquals(3, exBlits.getNumberOfElements());
		SecurityContextHolder.setCurrentUser(user);
		exBlits = exService.currentUserExchangeBlits(pageable);
		assertEquals(2, exBlits.getNumberOfElements());
	}
	
	@Test
	public void changeState()
	{
		createExBlitVmodel1 = exService.create(createExBlitVmodel1);
		exRepo.findOne(createExBlitVmodel1.getExchangeBlitId()).setOperatorState(OperatorState.APPROVED.name());
		ExchangeBlitChangeStateViewModel vmodel = new ExchangeBlitChangeStateViewModel();
		vmodel.setExchangeBlitId(createExBlitVmodel1.getExchangeBlitId());
		vmodel.setState(State.SOLD);
		exService.changeState(vmodel);
		assertEquals(State.SOLD.name(), exRepo.findOne(createExBlitVmodel1.getExchangeBlitId()).getState());
		vmodel.setState(State.CLOSED);
		exService.changeState(vmodel);
		assertEquals(State.CLOSED.name(), exRepo.findOne(createExBlitVmodel1.getExchangeBlitId()).getState());
	}
}
