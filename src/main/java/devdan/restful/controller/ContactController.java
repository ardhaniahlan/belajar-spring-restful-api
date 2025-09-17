package devdan.restful.controller;

import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.ContactResponse;
import devdan.restful.model.CreateContactRequest;
import devdan.restful.model.UpdateContactRequest;
import devdan.restful.model.WebResponse;
import devdan.restful.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user,@RequestBody CreateContactRequest request){
        ContactResponse response = contactService.create(user, request);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/api/contacts/{idContact}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> get(User user,@PathVariable("idContact") String id){
        ContactResponse response = contactService.get(user, id);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/contacts/{idContact}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> update(
            User user ,
            @RequestBody UpdateContactRequest request,
            @PathVariable("idContact") String id
    ){
        request.setId(id);

        ContactResponse response = contactService.update(user, request);
        return WebResponse.<ContactResponse>builder().data(response).build();
    }
}
