import React from "react";
import { createStackNavigator } from "@react-navigation/stack";
import Home from "../screens/Home";
import Payment from "../screens/Payment";

const Stack = createStackNavigator();

export const AppStack = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="HomeScreen"
        component={Home}
        options={{ headerShown: true, title: "Home Screen" }}
      />
      <Stack.Screen
        name="PaymentScreen"
        component={Payment}
        options={{ headerShown: true, title: "Payment Screen" }}
      />
    </Stack.Navigator>
  );
};
