import { useStripe } from '@stripe/stripe-react-native';
import {
  Button,
  Layout,
  Text,
  Modal,
  Card,
  Input,
} from '@ui-kitten/components';
import React, { useState } from 'react';
import { Image, StyleSheet, View } from 'react-native';
import MapView, { Marker } from 'react-native-maps';
import MapViewDirections from 'react-native-maps-directions';
import { MAPS_API_KEY } from '../Config';
import Toast from 'react-native-toast-message';
import { fetchPaymentSheetParams } from '../services/payment';
import { HOME_NAVIGATOR, HOME_SCREEN } from '../routes/AppRoutes';

function Payment({ navigation, route }) {
  const { id, ride } = route.params;
  const startAddressLatitude = ride?.startAddress?.latitude;
  const startAddressLongitude = ride?.startAddress?.longitude;
  const endAddressLatitude = ride?.endAddress?.latitude;
  const endAddressLongitude = ride?.endAddress?.longitude;

  const originAddress =
    ride?.startAddress?.street +
    ', ' +
    ride?.startAddress?.city +
    ', ' +
    ride?.startAddress?.state +
    ', ' +
    ride?.startAddress?.zipcode;

  const destinationAddress =
    ride?.endAddress?.street +
    ', ' +
    ride?.endAddress?.city +
    ', ' +
    ride?.endAddress?.state +
    ', ' +
    ride?.endAddress?.zipcode;

  const rideDate = new Date(ride?.rideTime).toDateString();
  const rideTime = new Date(ride?.rideTime).toLocaleTimeString();

  const startLocation = {
    latitude: startAddressLatitude,
    longitude: startAddressLongitude,
  };

  const endLocation = {
    latitude: endAddressLatitude,
    longitude: endAddressLongitude,
  };

  const oLat = Math.abs(startLocation.latitude);
  const oLng = Math.abs(startLocation.longitude);
  const dLat = Math.abs(endLocation.latitude);
  const dLng = Math.abs(endLocation.longitude);

  const initRegion = {
    latitude: (startLocation.latitude + endLocation.latitude) / 2,
    longitude: (startLocation.longitude + endLocation.longitude) / 2,
    latitudeDelta: Math.abs(oLat - dLat) + 0.3,
    longitudeDelta: Math.abs(oLng - dLng) + 0.3,
  };

  const styles = StyleSheet.create({
    map: {
      width: '100%',
      height: '50%',
      marginTop: '10%',
    },
    backdrop: {
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    detailsModal: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
    },
    detailsCard: {
      display: 'flex',
      justifyContent: 'space-between',
      width: '80%',
    },
  });

  const { initPaymentSheet, presentPaymentSheet } = useStripe();
  const [loading, setLoading] = useState(false);

  const initializePaymentSheet = async () => {
    const { paymentIntent, ephemeralKey, customer, publishableKey } =
      await fetchPaymentSheetParams(id);

    const { error } = await initPaymentSheet({
      customerId: customer,
      customerEphemeralKeySecret: ephemeralKey,
      paymentIntentClientSecret: paymentIntent,
      publishableKey: publishableKey,
    });
    if (!error) {
      setLoading(true);
    } else {
      console.log('Error in initializePaymentSheet', error);
    }
  };

  const openPaymentSheet = async () => {
    setLoading(true);
    await initializePaymentSheet();
    const result = await presentPaymentSheet();

    if (result.error) {
      Toast.show({ text1: `Error code: ${error.code}` });
    } else {
      setLoading(false);
      Toast.show({ text1: 'Your order is confirmed!' });
      navigation.navigate(HOME_SCREEN);
    }
  };

  return (
    <Layout>
      <View
        style={{
          backgroundColor: 'white',
          width: '100%',
          height: '100%',
          justifyContent: 'space-between',
          padding: 10,
        }}
      >
        <View style={{ alignItems: 'center', margin: '5%' }}>
          <Text category="h6">Ride with </Text>
          <Text category="s1">
            {' '}
            {ride.user?.firstName} {ride.user?.lastName}{' '}
          </Text>
        </View>
        <View style={{ marginTop: -40 }}>
          <MapView style={styles.map} region={initRegion}>
            <Marker coordinate={startLocation} />
            <Marker coordinate={endLocation} pinColor={'blue'} />
            <MapViewDirections
              origin={startLocation}
              destination={endLocation}
              apikey={MAPS_API_KEY}
              strokeWidth={3}
              strokeColor="hotpink"
            />
          </MapView>
        </View>
        <View
          style={{
            marginTop: -120,
            height: 140,
            flexDirection: 'column',
            justifyContent: 'space-around',
          }}
        >
          <Text category="h6"> Trip Details: </Text>
          <Layout
            style={{ flexDirection: 'row', justifyContent: 'space-between' }}
          >
            <Text style={{ fontWeight: 'bold' }}> Ride Time: </Text>
            <Text>
              {' '}
              {rideDate} {rideTime}{' '}
            </Text>
          </Layout>
          <Layout
            style={{ flexDirection: 'row', justifyContent: 'space-between' }}
          >
            <Text style={{ fontWeight: 'bold' }}> Origin: </Text>
            <Text> {originAddress} </Text>
          </Layout>
          <Layout
            style={{ flexDirection: 'row', justifyContent: 'space-between' }}
          >
            <Text style={{ fontWeight: 'bold' }}> Destination:</Text>
            <Text> {destinationAddress} </Text>
          </Layout>
          <Layout
            style={{ flexDirection: 'row', justifyContent: 'space-between' }}
          >
            <Text style={{ fontWeight: 'bold' }}> Capacity:</Text>
            <Text>
              {' '}
              {ride?.noPassengers - ride?.capacity}/{ride?.noPassengers}{' '}
            </Text>
          </Layout>
          <Layout
            style={{ flexDirection: 'row', justifyContent: 'space-between' }}
          >
            <Text style={{ fontWeight: 'bold' }}> Price Per person:</Text>
            <Text> ${ride?.pricePerPerson} </Text>
          </Layout>
        </View>
        <Button disabled={loading} onPress={openPaymentSheet}>
          Pay
        </Button>
      </View>
    </Layout>
  );
}

export default Payment;
