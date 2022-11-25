package com.rideshare.payment.controller;

import com.rideshare.payment.facade.RideServiceFacade;
import com.rideshare.payment.model.CustomerData;
import com.rideshare.payment.model.Payment;
import com.rideshare.payment.model.PaymentStatus;
import com.rideshare.payment.service.ICustomerDataService;
import com.rideshare.payment.service.IPaymentService;
import com.rideshare.payment.webentity.CompleteRequest;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/webhook")
@Slf4j
public class WebhookController {

    @Autowired
    private ICustomerDataService customerDataService;

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private RideServiceFacade rideService;

    Logger logger = LoggerFactory.getLogger(WebhookController.class);
    public static final String PAYMENT_SUCCEEDED = "payment_intent.succeeded";
    public static final String WEBHOOK_SECRET = "whsec_b6cedda38621fad225a569c8ac0ac439a0705e963e16455b89f7d0a968f2eda4";

    @PostMapping
    public ResponseEntity handleWebhook(@RequestBody String payload, @RequestHeader HttpHeaders headers) throws Exception {
        try {
            String signature = headers.get("Stripe-Signature").get(0);

            Event event = Webhook.constructEvent(payload, signature, WEBHOOK_SECRET);

            StripeObject stripeObject = null;
            if (event.getData().getObject() != null) {
                stripeObject = event.getData().getObject();
            }

            switch (event.getType()) {
                case PAYMENT_SUCCEEDED:
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                    // set payment status to complete
                    Payment payment = paymentService.getByStripePaymentId(paymentIntent.getId());
                    paymentService.updateStatus(payment.getId(), PaymentStatus.COMPLETED);

                    // set request status to complete in Ride Service
                    Integer requestId = payment.getRequestId();
                    CompleteRequest completeRequest = new CompleteRequest();
                    completeRequest.setReceiptUrl(paymentIntent.getLatestChargeObject().getReceiptUrl());
                    completeRequest.setStripePaymentId(paymentIntent.getId());

                    rideService.completeRequest(requestId, completeRequest);

                    break;
                default:
                    logger.info("Unhandled stripe event type" + event.getType());
            }

            return ResponseEntity.status(200).build();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
