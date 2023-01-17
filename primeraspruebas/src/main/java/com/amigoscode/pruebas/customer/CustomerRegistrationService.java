package com.amigoscode.pruebas.customer;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    public void registerNewCustomer(CustomerRegistrationRequest request){
        //Phone Number is taken
        //If taken lets check if belongs to the same customer
        //2.1) If yes return
        //2.2) throw an exception
        //3) Save Customer
        String phoneNumber = request.getCustomer().getPhoneNumber();
        Optional<Customer> customerOptional = customerRepository.selectCustomerByPhoneNumber(phoneNumber);

        if(customerOptional.isPresent()){
            if(customerOptional.get() == request.getCustomer()){
                return;
            }
            throw new IllegalStateException(String.format("The phoneNumber [%s] can the same from another user", phoneNumber));
        }

       if(request.getCustomer().getId() == null){
           request.getCustomer().setId(UUID.randomUUID());
       }

        customerRepository.save(request.getCustomer());

    }
}
