import {
  Text,
  Input,
  Layout,
  Modal,
  Card,
  Button,
} from '@ui-kitten/components';
import Toast from "react-native-toast-message";

import React, { useEffect } from 'react';
import {
  View,
  Image,
  StyleSheet,
  KeyboardAvoidingView,
  ScrollView,
} from 'react-native';
import { fetchUserDetails } from '../services/fetchUserDetails';
import updateUserDetails from '../services/updateUserDetails';

export const UserInfo = () => {
  const [email, setEmail] = React.useState('');
  const [firstName, setFirstName] = React.useState('');
  const [lastName, setLastName] = React.useState('');
  const [contactNo, setContactNo] = React.useState('');
  const [visible, setVisible] = React.useState(false);
  const [id, setId] = React.useState(0);
  const [profileImage, setProfileImage] = React.useState('');

  useEffect(() => {
    getUserDetails();
  }, []);

  const styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
    },
    input: {
      flex: 1,
      margin: 2,
    },
  });

  const getUserDetails = async () => {
    const res = await fetchUserDetails();
    if (res?.response?.status == 401) {
      Toast.show({
        type: 'error',
        text1: 'Unauthorised',
      });
      return;
    }

    if (res?.response?.status == 500) {
      Toast.show({
        type: 'error',
        text1: 'Error Fetching Details',
      });
      return;
    }

    setEmail(res?.data?.email);
    setFirstName(res?.data?.firstName);
    setLastName(res?.data?.lastName);
    setContactNo(res?.data?.phoneNo);
    setId(res?.data?.id);
    setProfileImage(res?.data?.profileImage);
  };

  const saveUserInfo = async () => {
    const userDetails = {
      id,
      firstName,
      lastName,
      profileImage,
    }

    const res = await updateUserDetails(userDetails);
    if (res?.response?.status == 401) {
      Toast.show({
        type: 'error',
        text1: 'Unauthorised',
      });
      return;
    }

    if (res?.response?.status == 500) {
      Toast.show({
        type: 'error',
        text1: 'Error Updating Details',
      });
      return;
    }

    getUserDetails();

    Toast.show({
      type: 'success',
      text1: 'Updated User Details',
    });
    
  }

  return (
    <KeyboardAvoidingView enabled behavior="position">
      <View
        style={{
          height: '100%',
          width: '100%',
          alignItems: 'center',
          backgroundColor: 'white',
        }}
      >
        <Layout
          style={{
            height: '50%',
            backgroundColor: 'white',
            width: '100%',
            alignItems: 'center',
            flexDirection: 'column',
            justifyContent: 'space-around',
          }}
        >
          <Layout style={{ width: '90%', alignItems: 'center' }}>
            <Text>Email Address: </Text>
            <Input
              style={styles.input}
              value={email}
              disabled={true}
              placeholder="Email Address"
              onChangeText={(nextValue) => setEmail(nextValue)}
            />
          </Layout>
          <Layout style={{ width: '90%', alignItems: 'center' }}>
            <Text>First Name: </Text>
            <Input
              style={styles.input}
              value={firstName}
              placeholder="First Name"
              onChangeText={(nextValue) => setFirstName(nextValue)}
            />
          </Layout>
          <Layout style={{ width: '90%', alignItems: 'center' }}>
            <Text>Last Name: </Text>
            <Input
              style={styles.input}
              value={lastName}
              placeholder="Last Name"
              onChangeText={(nextValue) => setLastName(nextValue)}
            />
          </Layout>
          <Layout style={{ width: '90%', alignItems: 'center' }}>
            <Text>Contact Number: </Text>
            <Input
              disabled={true}
              maxLength={10}
              style={styles.input}
              value={contactNo}
              placeholder="Contact Number"
              onChangeText={(nextValue) => setContactNo(nextValue)}
            />
          </Layout>
        </Layout>
        <Layout
          style={{
            backgroundColor: 'red',
            borderRadius: '90%',
            height: 180,
            width: 180,
            marginTop: '10%',
          }}
        >
          <Image
            source={require('../assets/img_avatar.png')}
            style={{
              borderRadius: '90%',
              height: 180,
              width: 180,
            }}
          />
        </Layout>
        <Button style={{ marginTop: '2%' }} onPress={() => setVisible(true)}>
          Change Profile Pic
        </Button>

        <Button style={{ marginTop: '2%' }} status="warning" onPress={()=>saveUserInfo()}>
          Save Details
        </Button>

        <Modal
          visible={visible}
          backdropStyle={styles.backdrop}
          onBackdropPress={() => setVisible(false)}
        >
          <Card disabled={true}>
            <Text>Welcome to UI Kitten 😻</Text>
            <Button onPress={() => setVisible(false)}>DISMISS</Button>
          </Card>
        </Modal>
      </View>
    </KeyboardAvoidingView>
  );
};
