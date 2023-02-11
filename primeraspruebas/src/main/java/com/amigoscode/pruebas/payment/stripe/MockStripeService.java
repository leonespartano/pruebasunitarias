package com.amigoscode.pruebas.payment.stripe;

import com.amigoscode.pruebas.payment.CardPaymentCharge;
import com.amigoscode.pruebas.payment.CardPaymentCharger;
import com.amigoscode.pruebas.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "false"
)
public class MockStripeService implements CardPaymentCharger {
    @Override
    public CardPaymentCharge chargeCard(String cardSource,
                                        BigDecimal amount,
                                        Currency currency,
                                        String description) {
        return new CardPaymentCharge(true);
    }
}
