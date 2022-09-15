package com.rideshare.payment.controller;

import com.rideshare.payment.webentity.PaymentIntentData;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.EphemeralKeyCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    @Value("${app.stripe.publishableKey}")
    private String publishableKey;

    @Value("${app.stripe.apiKey}")
    private String apiKey;


    private Logger logger = LoggerFactory.getLogger(PaymentController.class);


    // TODO: take requestId as input and fetch that request/ride from ride service for payment amount
    @GetMapping(path = "/checkout")
    public ResponseEntity<PaymentIntentData> checkout() throws Exception {

        Stripe.apiKey = apiKey;

        // Get stripe customer id from DB based on the userID from AuthenticationPrinciple
        // OR
        // Create new stripe customer and save its info in db
        // TODO: Add metadata for customer
        CustomerCreateParams customerParams = CustomerCreateParams.builder().build();
        Customer customer = Customer.create(customerParams);

        // Get Ephemeral Key for the customer
        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setCustomer(customer.getId())
                .build();

        RequestOptions ephemeralKeyRequestOptions = new RequestOptions.RequestOptionsBuilder()
                .setStripeVersionOverride("2022-08-01")
                .build();

        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams, ephemeralKeyRequestOptions);


        PaymentIntentCreateParams.PaymentMethodOptions paymentMethodOptions = PaymentIntentCreateParams.PaymentMethodOptions.builder()
                .setCard(PaymentIntentCreateParams.PaymentMethodOptions.Card.builder().build())
                .build();


        // TODO: add metadata for the payment
        // TODO: add requestId in the metadata to associate payment with request
        // Create Payment Intent
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(1099L)
                .setCurrency("usd")
                .setCustomer(customer.getId())
                .setPaymentMethodOptions(paymentMethodOptions)
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        // TODO: save paymentIntentId, requestId, stripeCustomerId in db
        // Return data to open payment sheet on app
        PaymentIntentData paymentIntentData = new PaymentIntentData(
                paymentIntent.getClientSecret(),
                ephemeralKey.getSecret(),
                customer.getId(),
                publishableKey
        );

        return ResponseEntity.ok(paymentIntentData);
    }

}
