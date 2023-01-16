package com.amigoscode.pruebas.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSaveCustomer() {
        // Given
        String phoneNumber = "0000";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Mauricio", phoneNumber);
        underTest.save(customer);
        // When
        Optional<Customer> optionalCustomer = underTest.findById(id);
        // Then
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            //assertThat(c.getName()).isEqualTo(customer.getName());
                            //assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
                            //assertThat(c).isEqualTo(customer);
                            assertThat(c).usingRecursiveComparison()
                                    .isEqualTo(customer);
                        }
                );

    }


}