package devdan.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.response.ContactResponse;
import devdan.restful.model.request.CreateContactRequest;
import devdan.restful.model.request.UpdateContactRequest;
import devdan.restful.model.response.WebResponse;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setName("Test");
        user.setToken("aaaa");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000);
        userRepository.save(user);
    }

    @Test
    void testCreateContactBadRequest() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("");
        request.setEmail("ardhanexamplecom");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testCreateContactSuccess() throws Exception {
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("Ardhani");
        request.setLastname("Ahlan");
        request.setEmail("ardhan@example.com");
        request.setPhone("08123213132");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("Ardhani", response.getData().getFirstname());
            assertEquals("Ahlan", response.getData().getLastname());
            assertEquals("ardhan@example.com", response.getData().getEmail());
            assertEquals("08123213132", response.getData().getPhone());

            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void testGetContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts/11312")
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
    void testGetContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ardhani");
        contact.setLastName("Ahlan");
        contact.setEmail("ardhan@example.com");
        contact.setPhone("0812114134");
        contact.setUser(user);
        contactRepository.save(contact);

        mockMvc.perform(
                get("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(contact.getId(), response.getData().getId());
            assertEquals(contact.getFirstName(), response.getData().getFirstname());
            assertEquals(contact.getLastName(), response.getData().getLastname());
            assertEquals(contact.getEmail(), response.getData().getEmail());
            assertEquals(contact.getPhone(), response.getData().getPhone());
        });
    }

    @Test
    void testUpdateContactSuccess() throws Exception{
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ardhani");
        contact.setLastName("Ahlan");
        contact.setEmail("ardhan@example.com");
        contact.setPhone("0812114134");
        contact.setUser(user);
        contactRepository.save(contact);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstname("Ahlan");
        request.setLastname("Ardhani");
        request.setEmail("ahlan@example.com");
        request.setPhone("0812114134");

        mockMvc.perform(
                put("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(request.getFirstname(), response.getData().getFirstname());
            assertEquals(request.getLastname(), response.getData().getLastname());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertEquals(request.getPhone(), response.getData().getPhone());

            assertTrue(contactRepository.existsById(contact.getId()));
        });
    }

    @Test
    void testUpdateContactBadRequest() throws Exception{
        UpdateContactRequest request = new UpdateContactRequest();
        request.setFirstname("");
        request.setEmail("sda");

        mockMvc.perform(
                put("/api/contacts/34242423")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void testDeleteContactNotFound() throws Exception {
        mockMvc.perform(
                delete("/api/contacts/11312")
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
    void testDeleteContactSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setFirstName("Ardhani");
        contact.setLastName("Ahlan");
        contact.setEmail("ardhan@example.com");
        contact.setPhone("0812114134");
        contact.setUser(user);
        contactRepository.save(contact);

        mockMvc.perform(
                delete("/api/contacts/" + contact.getId())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals("OK", response.getData());
        });
    }

    @Test
    void testSearchContactNotFound() throws Exception {
        mockMvc.perform(
                get("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(0, response.getPagingResponse().getCurrentPage());
            assertEquals(0, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });
    }

    @Test
    void testSearchSuccess() throws Exception {
        User user = userRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++){
            Contact contact = new Contact();
            contact.setId(UUID.randomUUID().toString());
            contact.setFirstName("Ardhani" + i);
            contact.setLastName("Ahlan");
            contact.setEmail("ardhan@example.com");
            contact.setPhone("0812114134");
            contact.setUser(user);
            contactRepository.save(contact);
        }

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Ardhani")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("name", "Ahlan")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("email", "example.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "0812")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(10, response.getData().size());
            assertEquals(0, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });

        mockMvc.perform(
                get("/api/contacts")
                        .queryParam("phone", "0812")
                        .queryParam("page", "1000")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("X-API-TOKEN", "aaaa")
        ).andExpectAll(
                status().isOk()
        ).andDo( result -> {
            WebResponse<List<ContactResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertEquals(0, response.getData().size());
            assertEquals(1000, response.getPagingResponse().getCurrentPage());
            assertEquals(10, response.getPagingResponse().getTotalPage());
            assertEquals(10, response.getPagingResponse().getSize());
        });
    }
}