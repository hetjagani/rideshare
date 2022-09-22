package com.rideshare.payment.controller;

import com.rideshare.payment.exception.ForbiddenException;
import com.rideshare.payment.facade.RideServiceFacade;
import com.rideshare.payment.facade.StripeServiceFacade;
import com.rideshare.payment.model.PaymentStatus;
import com.rideshare.payment.security.UserPrincipal;
import com.rideshare.payment.service.IPaymentService;
import com.rideshare.payment.webentity.*;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
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
import java.util.Objects;

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

    @GetMapping
    public ResponseEntity<PaginatedEntity<Payment>> getAllPayments(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());

            List<com.rideshare.payment.model.Payment> paymentList = paymentService.getAllPaginated(userId, page, limit);

            List<Payment> responsePayments = new ArrayList<>();

            paymentList.stream().forEach((payment -> {
                Payment responsePayment = new Payment();
                try {
                    this.addPaymentDetails(payment, responsePayment, token);
                    responsePayments.add(responsePayment);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));


            return ResponseEntity.ok(new PaginatedEntity<>(responsePayments, page, limit));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Payment> getPaymentDetails(@PathVariable Integer id, @RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal user) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Integer userId = Integer.parseInt(user.getId());

            com.rideshare.payment.model.Payment payment = paymentService.getById(id);

            if (!Objects.equals(payment.getUserId(), userId)) {
                throw new ForbiddenException("user not allowed to fetch this payment");
            }

            com.rideshare.payment.webentity.Payment responsePayment = new com.rideshare.payment.webentity.Payment();

            this.addPaymentDetails(payment, responsePayment, token);

            return ResponseEntity.ok(responsePayment);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<PaymentIntentData> checkout(@RequestHeader HttpHeaders headers, @AuthenticationPrincipal UserPrincipal user, @RequestBody CheckoutRequest request) throws Exception {
        try {
            String token = headers.get("Authorization").get(0);
            Request rideRequest = rideService.getRequest(request.getRequestId(), token);

            Customer customer = stripeService.createCustomer(user);

            EphemeralKey ephemeralKey = stripeService.createEphemeralKey(customer.getId());

            Long amount = rideRequest.getRide().getPricePerPerson().longValue() * 100;

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, customer.getId(), request.getRequestId());

            // Save paymentIntentId, requestId, stripeCustomerId in db
            com.rideshare.payment.model.Payment paymentData = new com.rideshare.payment.model.Payment();
            paymentData.setStripePaymentId(paymentIntent.getId());
            paymentData.setRequestId(request.getRequestId());
            paymentData.setUserId(Integer.parseInt(user.getId()));
            paymentData.setStripeCustomerId(customer.getId());
            paymentData.setStatus(PaymentStatus.CREATED);

            com.rideshare.payment.model.Payment payment = paymentService.create(paymentData);

            // Return data to open payment sheet on app
            PaymentIntentData paymentIntentData = new PaymentIntentData(
                    paymentIntent.getClientSecret(),
                    ephemeralKey.getSecret(),
                    payment.getStripeCustomerId(),
                    publishableKey,
                    payment
            );

            return ResponseEntity.ok(paymentIntentData);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void addPaymentDetails(com.rideshare.payment.model.Payment payment, Payment responsePayment, String token) throws Exception {
        responsePayment.setId(payment.getId());
        responsePayment.setStripePaymentId(payment.getStripePaymentId());
        responsePayment.setStatus(payment.getStatus());
        responsePayment.setUserId(payment.getUserId());
        responsePayment.setRequestId(payment.getRequestId());
        responsePayment.setStripeCustomerId(payment.getStripeCustomerId());

        Customer stripeCustomer = stripeService.getCustomer(payment.getStripeCustomerId());
        PaymentIntent paymentIntent = stripeService.getPaymentIntent(payment.getStripePaymentId());

        responsePayment.setStripeCustomer(new StripeCustomer(stripeCustomer.getId(), stripeCustomer.getEmail(), stripeCustomer.getPhone()));
        responsePayment.setStripePaymentIntent(new StripePaymentIntent(paymentIntent.getId(), paymentIntent.getAmount(), paymentIntent.getCurrency()));

        Request request = rideService.getRequest(payment.getRequestId(), token);
        responsePayment.setRequest(request);
    }
}
