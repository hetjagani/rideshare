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
import { requestRide } from '../services/requestRide';
import Toast from 'react-native-toast-message';
import { PAYMENT_SCREEN } from '../routes/AppRoutes';

const RidePostDetails = ({ navigation, route }) => {
  const { post } = route.params;
  const startAddressLatitude = post?.ride?.startAddress?.latitude;
  const startAddressLongitude = post?.ride?.startAddress?.longitude;
  const endAddressLatitude = post?.ride?.endAddress?.latitude;
  const endAddressLongitude = post?.ride?.endAddress?.longitude;
  const [loading, setLoading] = useState(false);
  const [visible, setVisible] = useState(false);
  const [notes, setNotes] = useState('');

  const originAddress =
    post?.ride?.startAddress?.street +
    ', ' +
    post?.ride?.startAddress?.city +
    ', ' +
    post?.ride?.startAddress?.state +
    ', ' +
    post?.ride?.startAddress?.zipcode;

  const destinationAddress =
    post?.ride?.endAddress?.street +
    ', ' +
    post?.ride?.endAddress?.city +
    ', ' +
    post?.ride?.endAddress?.state +
    ', ' +
    post?.ride?.endAddress?.zipcode;

  const rideDate = new Date(post?.ride?.rideTime).toDateString();
  const rideTime = new Date(post?.ride?.rideTime).toLocaleTimeString();

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

  const openModal = () => {
    setNotes('');
    setVisible(true);
  };

  const createRideRequest = () => {
    const data = {
      rideId: post?.ride?.id,
      notes: notes,
    };

    requestRide(data).then((res) => {
      if (res?.response && res?.response.status != 200) {
        Toast.show({
          type: 'error',
          text1: 'Error Requesting Ride',
          text2: res?.response?.data?.message,
        });
        return;
      }
      setVisible(false);

      navigation.navigate(PAYMENT_SCREEN, res?.data);
    });
  };

  return (
    <View style={{ backgroundColor: 'white', width: '100%', height: '100%' }}>
      <View style={{ alignItems: 'center' }}>
        <Layout
          style={{
            justifyContent: 'center',
            backgroundColor: 'light gray',
            borderRadius: '90%',
            height: 120,
            width: 120,
            marginTop: '5%',
          }}
        >
          <Image
            onLoadStart={() => setLoading(true)}
            onLoadEnd={() => setLoading(false)}
            source={{
              uri: post?.ride?.user?.profileImage,
            }}
            style={{
              borderRadius: '90%',
              height: 120,
              width: 120,
            }}
          />
          {loading && <LoadingView />}
        </Layout>
      </View>
      <View style={{ alignItems: 'center', margin: '5%' }}>
        <Text>Ride with </Text>
        <Text category="h6">
          {' '}
          {post.user?.firstName} {post.user?.lastName}{' '}
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
            {post.ride?.noPassengers - post.ride?.capacity}/
            {post.ride?.noPassengers}{' '}
          </Text>
        </Layout>
        <Layout
          style={{ flexDirection: 'row', justifyContent: 'space-between' }}
        >
          <Text style={{ fontWeight: 'bold' }}> Price Per person:</Text>
          <Text> ${post.ride?.pricePerPerson} </Text>
        </Layout>
      </View>
      <View style={{ width: '100%', alignItems: 'center', marginTop: '3%' }}>
        <Button onPress={() => openModal()}> Request Ride </Button>
      </View>

      {/* Ride Request Modal */}
      <Modal
        visible={visible}
        backdropStyle={styles.backdrop}
        onBackdropPress={() => setVisible(false)}
        style={styles.detailsModal}
      >
        <Card style={styles.detailsCard}>
          <Text category="s1" style={{ margin: 5 }}>
            Add Notes:
          </Text>
          <Input
            placeholder="Enter notes for ride"
            value={notes}
            onChangeText={(nextValue) => setNotes(nextValue)}
            style={{ margin: 5 }}
          />
          <Button
            onPress={() => createRideRequest()}
            status="success"
            style={{ margin: 5 }}
          >
            {' '}
            Confirm Requesting Ride{' '}
          </Button>
        </Card>
      </Modal>
    </View>
  );
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

export default RidePostDetails;
