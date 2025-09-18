package devdan.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import devdan.restful.entity.Address;
import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.request.CreateAddressRequest;
import devdan.restful.model.request.UpdateAddressRequest;
import devdan.restful.model.response.AddressResponse;
import devdan.restful.model.response.WebResponse;
import devdan.restful.repository.AddressRepository;
import devdan.restful.repository.ContactRepository;
import devdan.restful.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setName("Test");
        user.setToken("aaaa");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("ssss");
        contact.setFirstName("Ardhani");
        contact.setLastName("Ahlan");
        contact.setEmail("ardhan@example.com");
        contact.setPhone("0812114134");
        contact.setUser(user);
        contactRepository.save(contact);
    }

    @Test
    void testCreateAddressSuccess() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setStreet("Telaga Murni");
        request.setCity("Bekasi");
        request.setProvince("Jawa Barat");
        request.setCountry("Indonesia");
        request.setPostalCode("12345");

        mockMvc.perform(
                post("/api/contacts/ssss/addresses" )
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void testCreateAddressBadRequest() throws Exception {
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/ssss/addresses" )
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testGetAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("ssss").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet("Telaga Murni");
        address.setCity("Bekasi");
        address.setProvince("Jawa Barat");
        address.setCountry("Indonesia");
        address.setPostalCode("12345");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                get("/api/contacts/ssss/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });
    }

    @Test
    void testGetAddressNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/11312/addresses/1432")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isNotFound()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testUpdateAddressSuccess() throws Exception {
        Contact contact = contactRepository.findById("ssss").orElseThrow();

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setStreet("Telaga Murni");
        address.setCity("Bekasi");
        address.setProvince("Jawa Barat");
        address.setCountry("Indonesia");
        address.setPostalCode("12345");
        address.setContact(contact);
        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("Sudirman");
        request.setCity("Jakarta");
        request.setProvince("DKI");
        request.setCountry("Konoha");
        request.setPostalCode("32131");

        mockMvc.perform(
                put("/api/contacts/ssss/addresses/" + address.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void testUpdateAddressBadRequest() throws Exception {
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contacts/ssss/addresses/sdasd" )
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }
}
