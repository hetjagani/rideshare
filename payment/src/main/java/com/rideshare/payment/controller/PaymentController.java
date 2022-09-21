package com.rideshare.payment.controller;

import com.rideshare.payment.facade.RideServiceFacade;
import com.rideshare.payment.facade.StripeServiceFacade;
import com.rideshare.payment.model.Payment;
import com.rideshare.payment.model.PaymentStatus;
import com.rideshare.payment.security.UserPrincipal;
import com.rideshare.payment.service.IPaymentService;
import com.rideshare.payment.webentity.CheckoutRequest;
import com.rideshare.payment.webentity.PaymentIntentData;
import com.rideshare.payment.webentity.Request;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    @Value("${app.stripe.publishableKey}")
    private String publishableKey;

    @Autowired
    private RideServiceFacade rideService;

    @Autowired
    private StripeServiceFacade stripeService;

    @Autowired
    private IPaymentService paymentService;

    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @PostMapping(path = "/checkout")
    public ResponseEntity<PaymentIntentData> checkout(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal user, @RequestBody CheckoutRequest request) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Request rideRequest = rideService.getRequest(request.getRequestId(), token);

            Customer customer = stripeService.createCustomer(user);

            EphemeralKey ephemeralKey = stripeService.createEphemeralKey(customer.getId());

            Long amount = rideRequest.getRide().getPricePerPerson().longValue() * 100;

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, customer.getId(), request.getRequestId());

            // Save paymentIntentId, requestId, stripeCustomerId in db
            Payment paymentData = new Payment();
            paymentData.setStripePaymentId(paymentIntent.getId());
            paymentData.setRequestId(request.getRequestId());
            paymentData.setUserId(Integer.parseInt(user.getId()));
            paymentData.setStripeCustomerId(customer.getId());
            paymentData.setStatus(PaymentStatus.CREATED);

            Payment payment = paymentService.create(paymentData);

            // Return data to open payment sheet on app
            PaymentIntentData paymentIntentData = new PaymentIntentData(
                    paymentIntent.getClientSecret(),
                    ephemeralKey.getSecret(),
                    payment.getStripeCustomerId(),
                    publishableKey
            );

            return ResponseEntity.ok(paymentIntentData);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
