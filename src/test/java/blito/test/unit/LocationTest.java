package blito.test.unit;

import com.blito.Application;
import com.blito.enums.ProfileType;
import com.blito.models.Location;
import com.blito.repositories.LocationRepository;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;


/**
 * @author Farzam Vatanzadeh
 * 2/6/18
 * Mailto : farzam.vat@gmail.com
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LocationTest {
    @Autowired
    private LocationRepository locationRepository;
    @Test
    public void location() {
        GeometryFactory geometryFactory = new GeometryFactory();
        Location location = new Location();
        location.setAddress("address");
        location.setBiography("Biography");
        location.setName("MyName");
        location.setProfileType(ProfileType.LOCATION.name());
        location.setLatitude(35.7299314);
        location.setLongitude(51.3911722);
//        location.setPoint(geometryFactory.createPoint(new Coordinate(35.7299314,51.3911722)));
//        location.getPoint().setSRID(4326);
        locationRepository.save(location);


        Location rooberoo = new Location();
        rooberoo.setAddress("valiasr");
        rooberoo.setBiography("bio");
        rooberoo.setName("Rooberoo mansion");
        rooberoo.setLatitude(35.7020512);
        rooberoo.setLongitude(51.4059);
        locationRepository.save(rooberoo);

//        assertEquals(1,locationRepository.findLocationsNear(35.7269005,51.3870523).size());

        assertEquals(1,locationRepository.findLocationsNearByRadius(35.7020512,51.4059,4D, new PageRequest(0,1)).size());

        locationRepository.findLocationsNearByRadius(35.7020512,
                51.4059,
                4D,
                new PageRequest(0,10))
                .forEach(locationViewModel -> System.out.println(locationViewModel.toString()));
//        assertEquals(1,locationRepository.findLocationsNear(filter));
    }
}
