import { Layout, Text } from "@ui-kitten/components";
import React from "react";
import { createStackNavigator } from '@react-navigation/stack';
import { SafeAreaProvider } from "react-native-safe-area-context";
import { AuthProvider } from "./contexts/AuthContext";
import { Router } from "./routes/Router";

const Stack = createStackNavigator();

function Root() {
  return (
    <SafeAreaProvider>
        <AuthProvider>
            <Router />
        </AuthProvider>
    </SafeAreaProvider>
  );
}

export default Root;
