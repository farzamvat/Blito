package blito.test.integration;

import com.blito.enums.HostType;
import com.blito.repositories.AddressRepository;
import com.blito.resourceUtil.ResourceUtil;
import com.blito.rest.viewmodels.address.AddressViewModel;
import com.blito.rest.viewmodels.eventhost.EventHostViewModel;
import com.blito.utils.test.util.AbstractEventRestControllerTest;
import com.blito.utils.test.util.AbstractRestControllerTest;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class EventHostRestIntegrationTest extends AbstractEventRestControllerTest {

    @Autowired
    private AddressRepository addressRepository;

    private AddressViewModel createAddress(String name,Long eventHostId,String address) {
        AddressViewModel addressViewModel = new AddressViewModel();
        addressViewModel.setLatitude(34.4);
        addressViewModel.setLongitude(53.123);
        addressViewModel.setName(name);
        addressViewModel.setAddress(address);
        addressViewModel.setEventHostId(eventHostId);

        Response response = givenRestIntegration()
                .body(addressViewModel)
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts/addresses");
        response.then().statusCode(200);
        return response.thenReturn().as(AddressViewModel.class);
    }

    private AddressViewModel createAddressUser(String name,Long eventHostId,String address) {
        AddressViewModel addressViewModel = new AddressViewModel();
        addressViewModel.setLatitude(35.342);
        addressViewModel.setLongitude(53.123);
        addressViewModel.setName(name);
        addressViewModel.setAddress(address);
        addressViewModel.setEventHostId(eventHostId);

        Response response = givenRestIntegrationUser()
                .body(addressViewModel)
                .post(getServerAddress() + "/api/blito/v1.0/event-hosts/addresses");
        response.then().statusCode(200);
        return response.thenReturn().as(AddressViewModel.class);
    }

    @Test
    public void createAddress_success() {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("event host for address")
                        .thenReturn()
                        .as(EventHostViewModel.class);
       createAddress("name test 1",eventHostViewModel.getEventHostId(),"address");
    }

    @Test
    public void updateAddress_success() {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("event host for update address")
                        .thenReturn()
                        .as(EventHostViewModel.class);

        AddressViewModel persistedAddress = createAddress("name test 2",eventHostViewModel.getEventHostId(),"address 2");
        persistedAddress.setName("khoone fifi");

        Response updateResponse = givenRestIntegration()
                .body(persistedAddress)
                .put(getServerAddress() + "/api/blito/v1.0/event-hosts/addresses");
        updateResponse.then().statusCode(200);
    }

    @Test
    public void deleteAddress_success() {
        EventHostViewModel eventHostViewModel =
                createEventHost_success("event host for deleting address")
                .thenReturn().as(EventHostViewModel.class);

        AddressViewModel persistedAddress = createAddress("deleteAddress",eventHostViewModel.getEventHostId(),"deletingAddress bonbast 2");

        Response deleteResponse = givenRestIntegration()
                .delete(getServerAddress() + "/api/blito/v1.0/event-hosts/addresses/" + persistedAddress.getId());
        deleteResponse.then().body("message",equalTo(ResourceUtil.getMessage(com.blito.enums.Response.SUCCESS)));
        assertNull(addressRepository.findOne(persistedAddress.getId()));
    }

    @Test
    public void getAllEventHostsAddresses_success() {
        EventHostViewModel newEventHostViewModel = new EventHostViewModel();
        newEventHostViewModel.setHostName("event host for getting all addresses");
        newEventHostViewModel.setDescription("description");
        newEventHostViewModel.setHostType(HostType.INDIVIDUAL);
        newEventHostViewModel.setTelephone("88002116");
        Response response =
                givenRestIntegrationUser()
                        .body(newEventHostViewModel)
                        .post(getServerAddress() + "/api/blito/v1.0/event-hosts");
        response.then().statusCode(201);
        newEventHostViewModel = response.thenReturn().as(EventHostViewModel.class);

        createAddressUser("first",newEventHostViewModel.getEventHostId(),"address");
        createAddressUser("second",newEventHostViewModel.getEventHostId(), "address");
        createAddressUser("third",newEventHostViewModel.getEventHostId(),"address");

        givenRestIntegrationUser()
            .get(getServerAddress() + "/api/blito/v1.0/event-hosts/addresses")
            .then().body("size()", equalTo(3));
    }
}
