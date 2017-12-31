package blito.test.unit;

import com.blito.Application;
import com.blito.exceptions.NotAllowedException;
import com.blito.exceptions.NotFoundException;
import com.blito.exceptions.UserActivatedException;
import com.blito.models.User;
import com.blito.repositories.UserRepository;
import com.blito.services.UserAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author Farzam Vatanzadeh
 * 12/31/17
 * Mailto : farzam.vat@gmail.com
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class UserAccountServiceTest {
    @Autowired
    private UserAccountService userAccountService;

    @Test(expected = UserActivatedException.class)
    public void retrySendingActivationKey_alreadyActivated_fail() {
        User alreadyActivatedUser = new User();
        alreadyActivatedUser.setEmail("farzam.vat@gmail.com");
        alreadyActivatedUser.setActive(true);
        alreadyActivatedUser.setBanned(false);
        UserRepository userRepository = Mockito.spy(UserRepository.class);
        Mockito.when(userRepository.findByEmail(alreadyActivatedUser.getEmail())).thenReturn(Optional.of(alreadyActivatedUser));
        userAccountService.setUserRepository(userRepository);
        userAccountService.retrySendingActivationKey(alreadyActivatedUser.getEmail());
    }

    @Test(expected = NotAllowedException.class)
    public void retrySendingActivationKey_bannedUser_fail() {
        User bannedUser = new User();
        bannedUser.setEmail("farzam.vat@gmail.com");
        bannedUser.setActive(false);
        bannedUser.setBanned(true);
        UserRepository userRepository = Mockito.spy(UserRepository.class);
        Mockito.when(userRepository.findByEmail(bannedUser.getEmail())).thenReturn(Optional.of(bannedUser));
        userAccountService.setUserRepository(userRepository);
        userAccountService.retrySendingActivationKey(bannedUser.getEmail());
    }

    @Test(expected = NotFoundException.class)
    public void retrySendingActivationKey_userNotFound_fail() {
        UserRepository userRepository = Mockito.spy(UserRepository.class);
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        userAccountService.setUserRepository(userRepository);
        userAccountService.retrySendingActivationKey("anyEmail@gmail.com");
    }

}
