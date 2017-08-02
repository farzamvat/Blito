package blito.test.integration;

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

@ActiveProfiles("test")
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ImageControllerTest {
	
	@Autowired
	ImageRepository imageRepository;
	
	private TestRestTemplate rest = new TestRestTemplate();
	
	@Before
	public void init() {
		Image image = new Image();
		image.setImageUUID("EVENT_PHOTO");
		image.setImageType(ImageType.EVENT_PHOTO.name());
		
		imageRepository.save(image);
	}
	
	@Test
	public void downloadImageTest() {
		
	}

}
