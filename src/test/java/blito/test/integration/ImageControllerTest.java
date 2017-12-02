package blito.test.integration;

import com.blito.configs.Constants;
import com.blito.enums.Response;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.image.ImageViewModel;
import com.blito.utils.test.util.AbstractRestControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.io.File;

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


	public ImageViewModel uploadMultipartFile_success() {
		io.restassured.response.Response response =
			givenRestIntegration()
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.when()
			.multiPart(new File("JenkinsFile"))
			.post(getServerAddress() + "/api/blito/v1.0/images/multipart/upload");
		response.then().statusCode(200);
		return response.then().extract().body().as(ImageViewModel.class);
	}

	@Test
	public void download_success() {
		ImageViewModel imageViewModel =
				uploadMultipartFile_success();
		givenRestIntegration()
				.when()
				.get(getServerAddress() + "/api/blito/v1.0/download/?id=" + imageViewModel.getImageUUID())
				.then()
				.statusCode(200);
	}

	@Test
	public void download_fail() {
		givenRestIntegration()
				.when()
				.get(getServerAddress() + "/api/blito/v1.0/download/?id=" +"hjhj")
				.then()
				.statusCode(400)
				.body("message",equalTo(ResourceUtil.getMessage(Response.IMAGE_NOT_FOUND)));
	}

	@Test
	public void uploadDefaultAdmin() {
		io.restassured.response.Response response =
				givenRestIntegration()
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
						.when()
						.multiPart(new File("JenkinsFile"))
						.formParam("defaultId", Constants.DEFAULT_EXCHANGEBLIT_PHOTO)
						.post(getServerAddress() + "/api/blito/v1.0/images/multipart/upload");
		response.then().statusCode(200);
	}
}
