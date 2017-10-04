package blito.test.unit;

import com.blito.Application;
import com.blito.services.Initiallizer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/*
    @author Farzam Vatanzadeh
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class ImportSchemaTest {
    @Autowired
    private Initiallizer initiallizer;

    @Test
    public void insertSalonTest() {
        initiallizer.insertSalonSchemasAndDataIntoDB();
    }
}
