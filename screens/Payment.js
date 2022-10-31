import { useStripe } from "@stripe/stripe-react-native";
import { Button, Layout, Text } from "@ui-kitten/components";
import React, { useEffect, useState } from "react";
import { Alert } from "react-native";
import { fetchPaymentSheetParams } from "../services/payment";

function Payment() {
  const { initPaymentSheet, presentPaymentSheet } = useStripe();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    initializePaymentSheet();
  }, []);

  const initializePaymentSheet = async () => {
    const { paymentIntent, ephemeralKey, customer, publishableKey } =
      await fetchPaymentSheetParams(1);

    const { error } = await initPaymentSheet({
      customerId: customer,
      customerEphemeralKeySecret: ephemeralKey,
      paymentIntentClientSecret: paymentIntent,
      publishableKey: publishableKey,
    });
    if (!error) {
      setLoading(true);
    } else {
      console.log("Error in initializePaymentSheet", error);
    }
  };

  const openPaymentSheet = async () => {
    const result = await presentPaymentSheet();

    if (result.error) {
      Alert.alert(`Error code: ${error.code}`, error.message);
    } else {
      Alert.alert("Success", "Your order is confirmed!");
    }
  };

  return (
    <Layout>
      <Text category="h1">Payment Screen</Text>
      <Button disabled={!loading} onPress={openPaymentSheet}>
        Pay
      </Button>
    </Layout>
  );
}

export default Payment;
