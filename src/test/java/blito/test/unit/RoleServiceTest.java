package blito.test.unit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

import com.blito.Application;
import com.blito.enums.AdminApiBusinessName;
import com.blito.enums.ApiBusinessName;
import com.blito.models.User;
import com.blito.repositories.PermissionRepository;
import com.blito.repositories.RoleRepository;
import com.blito.repositories.UserRepository;
import com.blito.rest.viewmodels.role.RoleViewModel;
import com.blito.services.RoleService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class RoleServiceTest {

	@Autowired
	PermissionRepository permissionRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleService roleService;
	@Autowired
	RoleRepository roleRepo;

	private User user = new User();
	private User operator = new User();

	private RoleViewModel roleVmodel = new RoleViewModel();

	@Before
	public void init() {

		user.setFirstname("Hasti");
		user.setEmail("hasti.sahabi@gmail.com");
		user.setMobile("09127976837");
		user = userRepo.save(user);

		operator.setFirstname("Farzam");
		operator.setEmail("fifi@gmail.com");
		operator.setMobile("09124337522");
		operator = userRepo.save(operator);

		roleVmodel.setName("OPERATOR");

		Set<String> names = new HashSet<String>(Arrays.asList(ApiBusinessName.API1.name(), ApiBusinessName.API2.name(),
				ApiBusinessName.API2.name(), AdminApiBusinessName.ADMINAPI1.name()));

		roleVmodel.setPermissionIds(permissionRepo.findByApiBusinessNameIn(names).stream().map(p -> p.getPermissionId())
				.collect(Collectors.toSet()));

	}

	@Test
	public void createRoleTest() {
		assertEquals(8, permissionRepo.count());
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		assertEquals(3, roleVmodel.getPermissionIds().size());
	}
	
	@Test
	public void assignRoleTest() {
		assertEquals(8, permissionRepo.count());
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		
		roleService.assignRole(roleVmodel.getRoleId(), operator.getUserId());
		operator = userRepo.findOne(operator.getUserId());
		assertEquals(1, operator.getRoles().size());
	}
	
	@Test
	public void getRolesTest() {
		assertEquals(8, permissionRepo.count());
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		
		Pageable pageable = new PageRequest(0,5);
		
		Page<RoleViewModel> roles = roleService.getRoles(pageable);
		
		assertEquals(3, roles.getNumberOfElements());
	}
	
	@Test
	public void getRoleByIdTest() {
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		
		roleVmodel = roleService.getRoleById(roleVmodel.getRoleId());
		assertEquals("OPERATOR", roleVmodel.getName());
		
		roleVmodel = roleService.getRoleById(1);
		assertEquals("ADMIN", roleVmodel.getName());
		
		roleVmodel = roleService.getRoleById(2);
		assertEquals("USER", roleVmodel.getName());
	}
	
	@Test
	public void editRoleTest() {
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		

		Set<String> editedNames = new HashSet<String>(Arrays.asList(ApiBusinessName.API1.name(), ApiBusinessName.API2.name(),
				ApiBusinessName.API3.name(), AdminApiBusinessName.ADMINAPI1.name()));

		roleVmodel.setPermissionIds(permissionRepo.findByApiBusinessNameIn(editedNames).stream().map(p -> p.getPermissionId())
				.collect(Collectors.toSet()));
		
		roleVmodel = roleService.editRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		assertEquals(4, roleVmodel.getPermissionIds().size());
	}
	
	@Test
	public void deleteRoleById() {
		roleVmodel = roleService.createRole(roleVmodel);
		assertEquals(3, roleRepo.count());
		
		roleService.deleteRoleById(roleVmodel.getRoleId());
		assertEquals(2, roleRepo.count());
	}
	

}
