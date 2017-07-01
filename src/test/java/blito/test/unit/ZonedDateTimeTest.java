package blito.test.unit;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
		
	}
}
