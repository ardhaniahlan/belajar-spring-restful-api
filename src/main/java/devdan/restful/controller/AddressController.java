package devdan.restful.controller;

import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.request.CreateAddressRequest;
import devdan.restful.model.response.AddressResponse;
import devdan.restful.model.response.WebResponse;
import devdan.restful.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{idContact}/addresses",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(
            User user,
            @RequestBody CreateAddressRequest request,
            @PathVariable("idContact") String id
    ){
        request.setIdContact(id);
        AddressResponse response = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }

    @GetMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(
            User user,
            @PathVariable("idContact") String idContact,
            @PathVariable("idAddress") String idAddress
    ){
        AddressResponse response = addressService.get(user, idContact, idAddress);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }
}
