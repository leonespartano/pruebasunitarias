package com.amigoscode.pruebas.payment;

import com.amigoscode.pruebas.customer.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class PaymentIntegrationTest {

    @Test
    void itShouldCreatePaymentSuccessfully() {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "0000000");
        // When
        // Then
    }
}
