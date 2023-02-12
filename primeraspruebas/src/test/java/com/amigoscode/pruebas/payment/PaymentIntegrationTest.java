package com.amigoscode.pruebas.payment;

import com.amigoscode.pruebas.customer.Customer;
import com.amigoscode.pruebas.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given a customer
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer(customerId, "James", "+447000000000");

        // Perform registration for a new customer
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        ResultActions customerRegResultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objecToJson(request)))
        );

        // Payment
        long paymentId = 1L;
        Payment payment = new Payment(
                paymentId,
                customerId,
                new BigDecimal("1000.00"),
                Currency.GBP,
                "X0X0X0X0",
                "Zakat"

        );

        // Payment request
        PaymentRequest paymentRequest = new PaymentRequest(payment);

        // When payment is sent

        ResultActions paymentResultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objecToJson(paymentRequest)))
        );
        // Then both customer registration and payment request are 200 status code
        customerRegResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(status().isOk());

        //Payment is stored in db
        assertThat(paymentRepository.findById(paymentId))
                .isPresent()
                .hasValueSatisfying(p -> assertThat(p).isEqualToComparingFieldByField(payment)
        );
    }

    private String objecToJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e){
            fail("Failed to convert an object to json");
            return null;
        }
    }
}
