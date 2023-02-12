package com.amigoscode.pruebas.customer;

import com.amigoscode.pruebas.utils.PhoneNumberValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerRegistrationService {

    private final PhoneNumberValidator phoneNumberValidator;
    private final CustomerRepository customerRepository;

    public void registerNewCustomer(CustomerRegistrationRequest request){
        // 1. PhoneNumber is taken
        // 2. If taken lets check if belongs to same customer
        // - 2.1 if yes return
        // - 2.2 throw an exception
        // 3. Save customer
        String phoneNumber = request.getCustomer().getPhoneNumber();

        // Validate that phone number is valid
        if(!phoneNumberValidator.test(phoneNumber)){
            throw new IllegalStateException("Phone Number " + phoneNumber + " is not valid.");
        }

        Optional<Customer> customerOptional = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        if (customerOptional.isPresent()){
            Customer customer = customerOptional.get();
            if (customer.getName().equals(request.getCustomer().getName())){
                return;
            }
            throw new IllegalStateException(String.format("phone number [%s] is taken", phoneNumber));
        }

        if(request.getCustomer().getId() == null ){
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());
    }
}
