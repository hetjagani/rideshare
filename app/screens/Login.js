import { useNavigation } from '@react-navigation/native';
import React, { useState } from 'react';
import { globalStyles } from '../GlobalStyles';
import Toast from 'react-native-toast-message';

import { useAuth } from '../contexts/AuthContext';
import { Button, Icon, Input, Layout, Text } from '@ui-kitten/components';
import {
  ActivityIndicator,
  Image,
  TouchableWithoutFeedback,
  View,
} from 'react-native';

import { SIGNUP_SCREEN } from '../routes/AppRoutes';

const EMAIL_REGEX =
  /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

export const Login = () => {
  const navigation = useNavigation();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [secureTextEntry, setSecureTextEntry] = React.useState(true);

  const [loading, setIsLoading] = useState(false);
  const auth = useAuth();

  const toggleSecureEntry = () => {
    setSecureTextEntry(!secureTextEntry);
  };

  const renderIcon = (props) => (
    <TouchableWithoutFeedback onPress={toggleSecureEntry}>
      <Icon {...props} name={secureTextEntry ? 'eye-off' : 'eye'} />
    </TouchableWithoutFeedback>
  );

  const onSubmit = async () => {
    try {
      if (email == '' || !email.match(EMAIL_REGEX)) {
        Toast.show({
          type: 'error',
          text1: 'Invalid Email',
        });
        return;
      }

      if (password == '' || password.length < 8) {
        Toast.show({
          type: 'error',
          text1: 'Password should be minimum of 8 characters',
        });
        return;
      }
      const data = { email, password };
      setIsLoading(true);
      await auth.signIn(data);
      setIsLoading(false);
    } catch (error) {
      setIsLoading(false);
      console.log('Status', error.status);
    }
  };

  return (
    <View style={globalStyles.container}>
      {loading ? (
        <ActivityIndicator color={'#000'} animating={true} size="small" />
      ) : (
        <Layout
          style={{
            ...globalStyles.centeredContainer,
            justifyContent: 'space-evenly',
            alignItems: 'center',
            width: '70%',
            height: '70%'
          }}
        >
          <Image
            source={require('../assets/logo.jpg')}
            style={{ height: 50, width: 300 }}
          />
          <View
            style={{
              display: 'flex',
              width: '100%',
              justifyContent: 'space-between',
              height: '40%',
              alignItems: 'center',
            }}
          >
            <Text category="h1">Login</Text>
            <Input
              placeholder="Enter Email"
              onChangeText={(t) => setEmail(t)}
            />
            <Input
              placeholder="Enter Password"
              onChangeText={(t) => setPassword(t)}
              passwordRules="minlength:8"
              secureTextEntry={secureTextEntry}
              accessoryRight={renderIcon}
            />
            <Button onPress={() => onSubmit()} style={{ width: '100%' }}>
              Login
            </Button>

            <Text style={{ marginTop: 20 }}>
              New to Rideshare?{' '}
              <Text onPress={() => navigation.navigate(SIGNUP_SCREEN)}>
                Join now
              </Text>
            </Text>
          </View>
        </Layout>
      )}
    </View>
  );
};
