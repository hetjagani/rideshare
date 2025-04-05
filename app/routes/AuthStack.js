import React from "react";
import { createStackNavigator } from "@react-navigation/stack";
import { Login } from "../screens/Login";
import { LOGIN_SCREEN, SIGNUP_SCREEN } from "./AppRoutes";
import { Signup } from "../screens/Signup";

const Stack = createStackNavigator();

export const AuthStack = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name={LOGIN_SCREEN}
        component={Login}
        options={{ headerShown: false }}
      />
      <Stack.Screen
        name={SIGNUP_SCREEN}
        component={Signup}
        options={{ headerShown: false }}
      />
    </Stack.Navigator>
  );
};