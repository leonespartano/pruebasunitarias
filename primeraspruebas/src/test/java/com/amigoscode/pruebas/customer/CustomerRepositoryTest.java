package com.amigoscode.pruebas.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository undertest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "00000";
        Customer customer = new Customer(id, "Abel", phoneNumber);
        // When
        undertest.save(customer);
        // Then
        Optional<Customer> optionalCustomer = undertest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo("Abel");
//                    assertThat(c.getPhoneNumber()).isEqualTo("00000");
                            assertThat(c).isEqualToComparingFieldByField(customer);
                        }
                );
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberDoesNotExists() {
        // Given
        String phoneNumber = "00000";
        // When
        // Then
        Optional<Customer> optionalCustomer = undertest.selectCustomerByPhoneNumber("00000");
        assertThat(optionalCustomer).isNotPresent();
    }

    @Test
    void itShouldSaveCustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "00000");

        // When
        undertest.save(customer);

        // Then
        Optional<Customer> optionalCustomer = undertest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> {
//                    assertThat(c.getId()).isEqualTo(id);
//                    assertThat(c.getName()).isEqualTo("Abel");
//                    assertThat(c.getPhoneNumber()).isEqualTo("00000");
                    assertThat(c).isEqualToComparingFieldByField(customer);
                        }
                );
    }

    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null, "0000");
        //When
        //Then
        assertThatThrownBy(()->undertest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.name; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Alex", null);
        //When
        //Then
        assertThatThrownBy(()->undertest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.phoneNumber; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}