package com.amigoscode.pruebas.payment;

import com.amigoscode.pruebas.customer.Customer;
import com.amigoscode.pruebas.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;


class PaymentServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;

    private PaymentService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PaymentService(customerRepository, paymentRepository, cardPaymentCharger);
    }

    @Test
    void itShouldChargeCardSuccessfully() {
        // Given
        UUID customerId = UUID.randomUUID();
        // Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // Payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(true));

        // When
        underTest.chargeCard(customerId, paymentRequest);
        // Then
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);

        then(paymentRepository).should().save(paymentArgumentCaptor.capture());

        Payment paymentArgumentCaptoreValue = paymentArgumentCaptor.getValue();
        assertThat(paymentArgumentCaptoreValue).isEqualToIgnoringGivenFields(paymentRequest.getPayment(),
                "customerId");
        assertThat(paymentArgumentCaptoreValue.getCustomerId()).isEqualTo(customerId);
    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        // Given
        UUID customerId = UUID.randomUUID();
        // Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // Payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        Currency.USD,
                        "card123xx",
                        "Donation"
                )
        );

        // Card is charged successfully
        given(cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        )).willReturn(new CardPaymentCharge(false));

        // When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Card not debited for customer " + customerId);
        then(paymentRepository).should(never()).save(any(Payment.class));
    }

    @Test
    void itShouldNotChargeCardAndThrowWhenCurrencyIsNotSupported() {
        // Given
        UUID customerId = UUID.randomUUID();
        Currency eur = Currency.EUR;
        // Customer exist
        given(customerRepository.findById(customerId)).willReturn(Optional.of(mock(Customer.class)));

        // Payment request
        PaymentRequest paymentRequest = new PaymentRequest(
                new Payment(
                        null,
                        null,
                        new BigDecimal("100.00"),
                        eur,
                        "card123xx",
                        "Donation"
                )
        );


        // When
        assertThatThrownBy(() -> underTest.chargeCard(customerId, paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Currency ["+ eur + "] not supported");
        //Then
        // No interaction with cardPaymentCharger
        then(cardPaymentCharger).shouldHaveNoMoreInteractions();
        // No interaction with paymentRepository
        then(paymentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void itShouldNotChargeAndThrowWhenCustomerNotFound() {
        // Given
        UUID customerId = UUID.randomUUID();

        given(customerRepository.findById(customerId)).willReturn(Optional.empty());

        // When
        //Then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, new PaymentRequest(new Payment())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Customer with id ["+ customerId +"] not found");

        // No interactions with PaymentCharger not PaymentRepository
        then(cardPaymentCharger).shouldHaveNoMoreInteractions();
        then(paymentRepository).shouldHaveNoMoreInteractions();
    }
}