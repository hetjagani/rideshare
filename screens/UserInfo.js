import { Text, Input, Layout, Button, Spinner } from '@ui-kitten/components';
import Toast from 'react-native-toast-message';

import React, { useEffect, useState } from 'react';
import {
  View,
  Image,
  StyleSheet,
  KeyboardAvoidingView,
  Button as Save,
} from 'react-native';
import { fetchUserDetails } from '../services/fetchUserDetails';
import updateUserDetails from '../services/updateUserDetails';
import * as ImagePicker from 'expo-image-picker';
import { RNS3 } from 'react-native-aws3';
import uuid from 'react-native-uuid';
import {
  AWS_ACCESS_KEY,
  AWS_BUCKET,
  AWS_REGION,
  AWS_SECRET_KEY,
} from '../Config';

export const UserInfo = ({ navigation }) => {
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [contactNo, setContactNo] = useState('');
  const [id, setId] = useState(0);
  const [profileImage, setProfileImage] = useState('');
  const [image, setImage] = useState('');
  const [isUploading, setIsUploading] = useState(false);
  const [loading, setLoading] = useState(false);

  const LoadingIndicator = (props) => (
    <View style={[props.style, styles.indicator]}>
      <Spinner size="small" />
    </View>
  );

  navigation.setOptions({
    headerRight: () => <Save onPress={() => saveUserInfo()} title="Save" />,
  });

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
    setImage(res?.data?.profileImage);
  };

  const saveUserInfo = async () => {
    const userDetails = {
      id,
      firstName,
      lastName,
      profileImage: image,
    };

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
    navigation.goBack();
  };

  const pickImage = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });

    if (result.cancelled) {
      getUserDetails();
    }
    if (!result.canceled) {
      setImage(result.uri);
      uploadImageOnS3(result);
    }
  };

  const uploadImageOnS3 = async (image) => {
    const options = {
      keyPrefix: '',
      bucket: AWS_BUCKET,
      region: AWS_REGION,
      accessKey: AWS_ACCESS_KEY,
      secretKey: AWS_SECRET_KEY,
      successActionStatus: 201,
    };
    const file = {
      uri: `${image.uri}`,
      name: uuid.v4(),
      type: image.type,
    };

    return new Promise((resolve, reject) => {
      RNS3.put(file, options)
        .progress((e) => {
          setIsUploading(true);
          console.log(e.loaded / e.total);
        })
        .then((res) => {
          if (res.status === 201) {
            setIsUploading(false);
            const { postResponse } = res.body;
            setImage(postResponse.location);
            setProfileImage(postResponse.location);
            resolve({
              src: postResponse.location,
            });
          } else {
            alert('Error Uploading Image, try again');
            console.log('error uploading to s3', res);
          }
        })
        .catch((err) => {
          alert('Error Uploading Image, try again');
          console.log('error uploading to s3', err);
          reject(err);
        });
    });
  };

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
            backgroundColor: 'gray',
            borderRadius: '90%',
            height: 180,
            width: 180,
            marginTop: '10%',
          }}
        >
          <Image
            onLoadStart={() => setLoading(true)}
            onLoadEnd={() => setLoading(false)}
            source={{ uri: image }}
            style={{
              borderRadius: '90%',
              height: 180,
              width: 180,
            }}
          />
          {loading && <LoadingView />}
        </Layout>
        {isUploading === true ? (
          <Button
            style={{ margin: 2 }}
            appearance="outline"
            accessoryLeft={LoadingIndicator}
          >
            UPLOADING
          </Button>
        ) : (
          <Button style={{ marginTop: '2%' }} onPress={pickImage}>
            Change Profile Pic
          </Button>
        )}
      </View>
    </KeyboardAvoidingView>
  );
};
