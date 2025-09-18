package devdan.restful.service;

import devdan.restful.entity.Address;
import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.request.CreateAddressRequest;
import devdan.restful.model.response.AddressResponse;
import devdan.restful.model.response.ContactResponse;
import devdan.restful.repository.AddressRepository;
import devdan.restful.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getIdContact())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepository.save(address);
        return toAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String idContact, String idAddress){
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address){
        return AddressResponse.builder()
                .id(address.getId())
                .city(address.getCity())
                .street(address.getStreet())
                .country(address.getCountry())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .build();
    }
}
