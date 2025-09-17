package devdan.restful.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.ContactResponse;
import devdan.restful.model.CreateContactRequest;
import devdan.restful.model.WebResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}