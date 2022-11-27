import { Button, Input, Layout, Text, Spinner } from '@ui-kitten/components';
import * as ImagePicker from 'expo-image-picker';
import React, { useState } from 'react';
import {
  KeyboardAvoidingView,
  ScrollView,
  StyleSheet,
  View,
} from 'react-native';
import DatePicker from 'react-native-modern-datepicker';
import Toast from 'react-native-toast-message';
import { PROFILE_SCREEN } from '../routes/AppRoutes';
import verifyDocument from '../services/verifyDocument';

export default function UpdateRole({ navigation }) {
  const styles = StyleSheet.create({
    container: {
      display: 'flex',
      height: '100%',
      alignItems: 'center',
      padding: 10,
    },
    rideForm: {
      width: '80%',
      marginTop: 30,
    },
    rideInput: {
      marginTop: 20,
    },
  });

  const [address, setAddress] = useState('');
  const [documentNumber, setDocumentNumber] = useState('');
  const [name, setName] = useState('');
  const [postcode, setPostcode] = useState(0);
  const [dob, setDob] = useState(new Date());
  const [isVerifying, setIsVerifying] = useState(false);
  const [image, setImage] = useState('');
  const [showDatePicker, setShowDatePicker] = useState(false);

  const toggleDatePicker = () => {
    setShowDatePicker(!showDatePicker);
  };

  const LoadingIndicator = (props) => (
    <View style={[props.style]}>
      <Spinner size="small" />
    </View>
  );
  const updateRoleToDriver = () => {
    setIsVerifying(true);
    const dataObj = {
      address,
      documentNumber,
      name,
      postcode,
      dob: dob.toString().substring(0, 10),
      fileBase64: image,
    };

    verifyDocument(dataObj).then((res) => {
      setIsVerifying(false);
      if (res?.response && res?.response.status != 200) {
        Toast.show({
          type: 'error',
          text1: 'Cannot Verify Details, Try Again!',
          text2: res?.response?.data?.message,
        });
        return;
      }
      navigation.navigate(PROFILE_SCREEN);

      Toast.show({
        type: 'success',
        text1: 'Hurray! Now you are a Driver',
      });
    });
  };

  const pickImage = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
      base64: true,
    });

    if (!result.cancelled) {
      setImage(result.base64);
      updateRoleToDriver();
    }
  };

  return (
    <KeyboardAvoidingView
      behavior="padding"
      style={{ height: '100%', backgroundColor: 'white' }}
    >
      <ScrollView style={{ backgroundColor: 'white' }}>
        <Layout style={styles.container}>
          <Text category="h1">Verify User Details</Text>
          <View style={styles.rideForm}>
            <Input
              label={'Registered Address (As on DL)'}
              onChangeText={(next) => setAddress(next)}
              style={styles.rideInput}
              value={address}
            />
            <Input
              label={'DL number'}
              onChangeText={(next) => setDocumentNumber(next)}
              style={styles.rideInput}
              value={documentNumber}
            />
            <Input
              label={'Full Name'}
              onChangeText={(next) => setName(next)}
              style={styles.rideInput}
              value={name}
            />
            <Input
              label={'Zipcode'}
              onChangeText={(next) => setPostcode(next)}
              style={styles.rideInput}
              value={postcode}
              keyboardType="numeric"
            />
            <View style={styles.rideInput}>
              <Button onPress={toggleDatePicker}>
                {showDatePicker == false ? 'Select Date of Birth' : 'Done'}
              </Button>
              {showDatePicker && (
                <DatePicker
                  onSelectedChange={(date) => {
                    setDob(date);
                  }}
                />
              )}
              <Text category="s1">DOB: {new Date(dob).toDateString()}</Text>
            </View>
            <View style={styles.rideInput}>
              {isVerifying === true ? (
                <Button
                  style={{ margin: 2 }}
                  appearance="outline"
                  accessoryLeft={LoadingIndicator}
                >
                  Verifying
                </Button>
              ) : (
                <Button style={{ marginTop: '2%' }} onPress={pickImage}>
                  Upload License
                </Button>
              )}
            </View>
          </View>
        </Layout>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}
