package blito.test.unit;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
	}
}
