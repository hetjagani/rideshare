import { Button, Layout, Text } from "@ui-kitten/components";
import React from "react";
import { useAuth } from "../contexts/AuthContext";

function Home({ navigation }) {
  const auth = useAuth();
  const signOut = () => {
    auth.signOut();
  };

  const payment = () => {
    navigation.navigate("PaymentScreen");
  };
  return (
    <Layout>
      <Text category="h1">Home Screen</Text>
      <Button onPress={() => signOut()}>SignOut</Button>
      <Button onPress={() => payment()}>Pay</Button>
    </Layout>
  );
}

export default Home;
