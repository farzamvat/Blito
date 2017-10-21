package blito.test.unit;

import com.blito.enums.Response;
import com.blito.exceptions.SeatException;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.utility.HandleUtility;
import com.blito.rest.viewmodels.blit.SeatErrorViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;

@ActiveProfiles("me")
@RunWith(SpringRunner.class)
public class ZonedDateTimeTest {


	@Test
	public void zoneTest()
	{
		ZonedDateTime tehran = ZonedDateTime.now(ZoneId.of("Asia/Tehran"));
		System.out.println(tehran);
		
		ZonedDateTime america = ZonedDateTime.now(ZoneId.of("America/Manaus"));
		System.out.println(america);
		
		String field = "eventHost_hostName";
		String field2 = "eventHost";
		
		if(field2.split("_").length == 1)
			assertTrue(true);
		else
			assertTrue(false);
		if(field.split("_").length == 2)
			assertTrue(true);
		else
			assertTrue(false);
		
		Arrays.asList(1).stream().reduce((a1,a2) -> a1 + a2).ifPresent(result -> System.out.println(result + "    *****************************************"));

		System.out.println(ResourceUtil.getMessage(Response.INVALID_USER_STATUS_TO_SEND_MSG));
	}
	@Test
	public void handleUtilityTest() {
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockHttpServletResponse= new MockHttpServletResponse();
		ResponseEntity<?> response = CompletableFuture.supplyAsync(() -> {
			throw new SeatException("message",new HashSet<>(Collections.singleton(new SeatErrorViewModel("uid","sold"))));
		}).handle((result,throwable) -> HandleUtility.generateResponseResult(() -> result,throwable,mockHttpServletRequest,mockHttpServletResponse)).join();

	}
}
