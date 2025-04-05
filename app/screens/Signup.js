import { Icon, Input, Layout, Text, Button } from "@ui-kitten/components";
import React, { useState } from "react";
import {
  ActivityIndicator,
  KeyboardAvoidingView,
  TouchableWithoutFeedback,
  View,
} from "react-native";
import { useAuth } from "../contexts/AuthContext";
import { globalStyles } from "../GlobalStyles";
import Toast from "react-native-toast-message";

export const Signup = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rePassword, setRePassword] = useState("");
  const [phoneNo, setPhoneNo] = useState("");
  const [loading, setIsLoading] = useState(false);
  const [secureTextEntry, setSecureTextEntry] = React.useState(true);

  const auth = useAuth();

  const toggleSecureEntry = () => {
    setSecureTextEntry(!secureTextEntry);
  };

  const renderIcon = (props) => (
    <TouchableWithoutFeedback onPress={toggleSecureEntry}>
      <Icon {...props} name={secureTextEntry ? "eye-off" : "eye"} />
    </TouchableWithoutFeedback>
  );

  const EMAIL_REGEX =
    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  const submitSignUp = async () => {
    try {
      if (email == "" || !email.match(EMAIL_REGEX)) {
        Toast.show({
          type: "error",
          text1: "Invalid Email",
        });
        return;
      }

      if (password == "" || password.length < 8) {
        Toast.show({
          type: "error",
          text1: "Password should be minimum of 8 characters",
        });
        return;
      }

      if (password != rePassword) {
        Toast.show({
          type: "error",
          text1: "Passwords entered do not match",
        });
        return;
      }

      const data = { email, password, phoneNo };
      setIsLoading(true);
      await auth.signUp(data);
      setIsLoading(false);
    } catch (error) {
      setIsLoading(false);
    }
  };

  return (
    <View style={globalStyles.container}>
      {loading ? (
        <ActivityIndicator color={"#000"} animating={true} size="small" />
      ) : (
        <KeyboardAvoidingView enabled behavior="padding">
          <Layout
            style={{
              ...globalStyles.centeredContainer,
              width: "70%",
            }}
          >
            <Text category="h1">Sign Up</Text>
            <Input
              style={globalStyles.input}
              placeholder="email"
              label="Email"
              onChangeText={(t) => setEmail(t)}
            />
            <Input
              style={globalStyles.input}
              label="Enter password"
              placeholder="password"
              onChangeText={(t) => setPassword(t)}
              passwordRules="minLength:8"
              secureTextEntry={secureTextEntry}
              accessoryRight={renderIcon}
            />
            <Input
              style={globalStyles.input}
              label="Re-enter password"
              placeholder="password"
              onChangeText={(t) => setRePassword(t)}
              passwordRules="minLength:8"
              secureTextEntry={secureTextEntry}
              accessoryRight={renderIcon}
            />
            <Input
              style={globalStyles.input}
              placeholder="phone number"
              label="Phone Number"
              onChangeText={(t) => setPhoneNo(t)}
            />
            <Button onPress={() => submitSignUp()}>Sign Up</Button>
          </Layout>
        </KeyboardAvoidingView>
      )}
    </View>
  );
};
