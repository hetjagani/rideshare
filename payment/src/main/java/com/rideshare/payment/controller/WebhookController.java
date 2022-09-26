package com.rideshare.payment.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/webhook")
public class WebhookController {

    Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping
    public ResponseEntity handleWebhook(@RequestBody String payload) throws Exception {
        JsonMapper jsonMapper = JsonMapper.builder().build();

        Event event = jsonMapper.readValue(payload, Event.class);

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                // get CustomerData for this customer
                // set payment status to complete
                // set request status to complete in Ride Service
                break;
            default:
                logger.info("Unhandled stripe event type" + event.getType());
        }

        return ResponseEntity.status(200).build();

    }

}
