import { useNavigation } from "@react-navigation/native";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { globalStyles } from "../GlobalStyles";

import { useAuth } from "../contexts/AuthContext";
import { Button, Input, Layout, Text } from "@ui-kitten/components";

const EMAIL_REGEX =
  /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export const Login = () => {
  const navigation = useNavigation();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setIsLoading] = useState(false);
  const auth = useAuth();

  const {
    handleSubmit,
    control,
    formState: { errors },
  } = useForm();

  const onSubmit = async () => {
    try {
      const data = { email, password };
      setIsLoading(true);
      await auth.signIn(data);
      setIsLoading(false);
    } catch (error) {
      setIsLoading(false);
      console.log(error);
    }
  };

  return (
    <Layout style={globalStyles.centeredContainer}>
      <Text category="h1">Login</Text>
      <Input placeholder="email" onChangeText={(t) => setEmail(t)} />
      <Input
        placeholder="password"
        onChangeText={(t) => setPassword(t)}
        secureTextEntry={true}
      />
      <Button onPress={() => onSubmit()}>Login</Button>
    </Layout>
  );
};
