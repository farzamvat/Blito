package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.enums.AdminApiBusinessName;
import com.blito.enums.ApiBusinessName;
import com.blito.models.Role;
import com.blito.models.User;
import com.blito.repositories.PermissionRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.role.RoleViewModel;
import com.blito.services.RoleService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
// @Transactional
public class RoleServiceTest {

	@Autowired
	PermissionRepository permissionRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleService roleService;

	private User user = new User();
	private User operator = new User();

	private Role operatorRole = new Role();
	private RoleViewModel roleVmodel = new RoleViewModel();

	@Before
	public void init() {

		user.setFirstname("Hasti");
		user.setEmail("hasti.sahabi@gmail.com");
		user.setMobile("09127976837");
		userRepo.save(user);

		operator.setFirstname("Farzam");
		operator.setEmail("fifi@gmail.com");
		operator.setMobile("09124337522");
		userRepo.save(operator);

		roleVmodel.setName("OPERATOR");

		Set<String> names = new HashSet<String>(Arrays.asList(ApiBusinessName.API1.name(), ApiBusinessName.API2.name(),
				ApiBusinessName.API2.name(), AdminApiBusinessName.ADMINAPI1.name()));

		roleVmodel.setPermissionIds(permissionRepo.findByApiBusinessNameIn(names).stream().map(p -> p.getPermissionId())
				.collect(Collectors.toSet()));

	}

	@Test
	public void testCreateRole() {
		assertEquals(5, permissionRepo.count());

	}

}
