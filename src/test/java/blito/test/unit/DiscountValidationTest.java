package blito.test.unit;
/*
    @author Farzam Vatanzadeh
*/

import com.blito.rest.viewmodels.discount.DiscountViewModel;
import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class DiscountValidationTest {

    private LocalValidatorFactoryBean localValidatorFactoryBean;
    @Before
    public void init() {
        localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setProviderClass(HibernateValidator.class);
        localValidatorFactoryBean.afterPropertiesSet();
    }

    @Test
    public void testDiscountRegex() {
        DiscountViewModel discountViewModel = new DiscountViewModel();
        discountViewModel.setPercent(true);
        discountViewModel.setCode("123123123DiscountCode");
        discountViewModel.setExpirationDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).plusDays(1).toInstant()));
        discountViewModel.setEffectDate(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Tehran")).minusDays(1).toInstant()));
        discountViewModel.setReusability(10);
        discountViewModel.setPercentage(30D);
        discountViewModel.setAmount(0L);
        discountViewModel.setBlitTypeIds(Collections.singleton(1L));

        Set<ConstraintViolation<Object>> c = localValidatorFactoryBean.validate(discountViewModel);
        assertEquals(0,c.size());
    }

}
