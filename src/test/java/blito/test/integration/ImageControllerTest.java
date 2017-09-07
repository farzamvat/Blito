package blito.test.integration;

import com.blito.configs.Constants;
import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.blito.Application;
import com.blito.enums.ImageType;
import com.blito.models.Image;
import com.blito.repositories.ImageRepository;

import static org.hamcrest.core.IsEqual.equalTo;

public class ImageControllerTest extends AbstractRestControllerTest {

	@Test
	public void downloadImage_success() {
		givenRestIntegration()
				.when()
				.get(getServerAddress() + "/api/blito/v1.0/download?id="+ Constants.DEFAULT_HOST_PHOTO)
				.then()
				.statusCode(200);
	}

	@Test
	public void downloadDefaulExchangeBlitPhoto_notfound() {
		givenRestIntegration()
				.when()
				.get(getServerAddress() + "/api/blito/v1.0/download?id="+ Constants.DEFAULT_EXCHANGEBLIT_PHOTO)
				.then()
				.statusCode(400).body("message",equalTo(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
	}
}
