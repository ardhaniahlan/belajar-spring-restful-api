package devdan.restful.service;

import devdan.restful.entity.Address;
import devdan.restful.entity.Contact;
import devdan.restful.entity.User;
import devdan.restful.model.request.CreateAddressRequest;
import devdan.restful.model.request.UpdateAddressRequest;
import devdan.restful.model.response.AddressResponse;
import devdan.restful.repository.AddressRepository;
import devdan.restful.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request){
        validationService.validate(request);

        Contact contact = contactRepository.findFirstByUserAndId(user, request.getIdContact())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, request.getIdAddress())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        address.setCity(request.getCity());
        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional
    public void remove(User user, String idContact, String idAddress){
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String idContact){
        Contact contact = contactRepository.findFirstByUserAndId(user, idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found"));

        List<Address> addressList = addressRepository.findAllByContact(contact);
        return addressList.stream().map(this::toAddressResponse).toList();
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
