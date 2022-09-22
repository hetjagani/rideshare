package com.rideshare.payment.facade;

import com.rideshare.payment.model.CustomerData;
import com.rideshare.payment.model.Payment;
import com.rideshare.payment.security.UserPrincipal;
import com.rideshare.payment.service.ICustomerDataService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class StripeServiceFacade {

    @Value("${app.stripe.apiKey}")
    private String apiKey;

    @Autowired
    private ICustomerDataService customerDataService;

    private Logger logger = LoggerFactory.getLogger(StripeServiceFacade.class);

    public StripeServiceFacade() {
        logger.info("Initialized stripe service facade with API key: " + this.apiKey);
        Stripe.apiKey = this.apiKey;
    }

    public Customer createCustomer(UserPrincipal user) throws Exception {
        Stripe.apiKey = this.apiKey;
        Integer userId = Integer.parseInt(user.getId());
        if(customerDataService.userExists(userId)) {
            // retrieve stripe customer info
            CustomerData data = customerDataService.getByUserId(userId);
            return Customer.retrieve(data.getStripeCustomerId());
        } else {
            // create stripe customer
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(user.getEmail())
                    .setName(user.getName())
                    .setPhone(user.getPhoneNo())
                    .build();
            Customer customer = Customer.create(customerParams);

            // Save customer data
            CustomerData data = new CustomerData(userId, customer.getId());
            customerDataService.create(data);

            return customer;
        }

    }

    public EphemeralKey createEphemeralKey(String customerId) throws StripeException {
        Stripe.apiKey = this.apiKey;
        // Get Ephemeral Key for the customer
        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setCustomer(customerId)
                .build();

        RequestOptions ephemeralKeyRequestOptions = new RequestOptions.RequestOptionsBuilder()
                .setStripeVersionOverride("2022-08-01")
                .build();

        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams, ephemeralKeyRequestOptions);
        return ephemeralKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String customerId, Integer requestId) throws StripeException {
        Stripe.apiKey = this.apiKey;
        PaymentIntentCreateParams.PaymentMethodOptions paymentMethodOptions = PaymentIntentCreateParams.PaymentMethodOptions.builder()
                .setCard(PaymentIntentCreateParams.PaymentMethodOptions.Card.builder().build())
                .build();

        // Create Payment Intent
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .putMetadata("requestId", Integer.toString(requestId))
                .setAmount(amount)
                .setCurrency("usd")
                .setCustomer(customerId)
                .setPaymentMethodOptions(paymentMethodOptions)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);
        return paymentIntent;
    }

    public PaymentIntent getPaymentIntent(String id) throws StripeException {
        Stripe.apiKey = this.apiKey;
        return PaymentIntent.retrieve(id);
    }

    public Customer getCustomer(String id) throws StripeException {
        Stripe.apiKey = this.apiKey;
        return Customer.retrieve(id);
    }
}
