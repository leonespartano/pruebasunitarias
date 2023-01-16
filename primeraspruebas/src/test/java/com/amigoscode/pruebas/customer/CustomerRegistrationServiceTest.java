package com.amigoscode.pruebas.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerRegistrationService underTest;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "0000";
        Customer customer = new Customer(null, "Lalo", phoneNumber);    
        CustomerRegistrationService request = new CustomerRegistrationService (customer);
        // When
        underTest.registerNewCustomer();
        // Then
    }
}