package devdan.restful.controller;

import devdan.restful.entity.User;
import devdan.restful.model.request.CreateContactRequest;
import devdan.restful.model.request.SearchContactRequest;
import devdan.restful.model.request.UpdateContactRequest;
import devdan.restful.model.response.ContactResponse;
import devdan.restful.model.response.PagingResponse;
import devdan.restful.model.response.WebResponse;
import devdan.restful.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping(
            path = "/api/contacts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ContactResponse> create(User user, @RequestBody CreateContactRequest request){
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

    @DeleteMapping(
            path = "/api/contacts/{idContact}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user,@PathVariable("idContact") String id){
        contactService.delete(user, id);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ContactResponse>> search(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ){
        SearchContactRequest request = SearchContactRequest.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .page(page)
                .size(size)
                .build();

        Page<ContactResponse> responsePage = contactService.search(user, request);
        return WebResponse.<List<ContactResponse>>builder()
                .data(responsePage.getContent())
                .pagingResponse(PagingResponse.builder()
                        .currentPage(responsePage.getNumber())
                        .totalPage(responsePage.getTotalPages())
                        .size(responsePage.getSize())
                        .build())
                .build();
    }
}
