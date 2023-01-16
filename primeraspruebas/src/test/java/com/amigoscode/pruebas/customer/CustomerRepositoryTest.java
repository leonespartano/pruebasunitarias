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
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        String phoneNumber = "0000";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Mauricio", phoneNumber);
        underTest.save(customer);
        // When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        // Then
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(
                        c -> {
                            //assertThat(c.getName()).isEqualTo(customer.getName());
                            //assertThat(c.getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
                            //assertThat(c).isEqualTo(customer);
                            assertThat(c).isEqualToComparingFieldByField(customer);
                            //assertThat(c).usingRecursiveComparison()
                                    //.isEqualTo(customer);
                        }
                );
    }

    @Test
    void itShouldNotSelectCustomerByPhoneNumberWhenNumberDoesNotExist() {
        // Given
        String phoneNumber = "0000";
        // When
        Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
        // Then
        assertThat(optionalCustomer).isNotPresent();
    }

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
    @Test
    void itShouldNotSaveCustomerWhenNameIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0000";
        Customer customer = new Customer(id, null, phoneNumber);
        // When
        // Then
        assertThatThrownBy(()->underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.name; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberIsNull() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0000";
        Customer customer = new Customer(id, "Lulo", null);
        // When
        // Then
        assertThatThrownBy(()->underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.phoneNumber; nested exception is org.hibernate.PropertyValueException: not-null property references a null or transient value : com.amigoscode.pruebas.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}