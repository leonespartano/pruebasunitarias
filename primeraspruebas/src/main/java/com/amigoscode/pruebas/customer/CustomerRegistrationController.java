package com.amigoscode.pruebas.customer;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/registration")
public class CustomerRegistrationController {

    public void registerNewCustomer(@Valid @RequestBody CustomerRegistrationRequest request){

    }
}