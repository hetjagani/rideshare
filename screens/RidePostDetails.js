import { Layout, Text } from '@ui-kitten/components';
import React, { useState } from 'react';
import { Image, StyleSheet, View } from 'react-native';
import MapView, { Marker } from 'react-native-maps';
import MapViewDirections from 'react-native-maps';

const RidePostDetails = ({ route }) => {
  const { post } = route.params;
  const startAddressLatitude = post?.ride?.startAddress?.latitude;
  const startAddressLongitude = post?.ride?.startAddress?.longitude;
  const endAddressLatitude = post?.ride?.endAddress?.latitude;
  const endAddressLongitude = post?.ride?.endAddress?.longitude;
  const [loading, setLoading] = useState(false);
  var mapView = null;
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
  const capacity = post?.ride?.capacity;

  const startLocation = {
    latitude: startAddressLatitude,
    longitude: startAddressLongitude,
  };

  const initialRegion = {
    ...startLocation,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421,
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

  return (
    <View style={{ backgroundColor: 'white', width: '100%', height: '100%' }}>
      <View style={{ alignItems: 'center' }}>
        <Layout
          style={{
            justifyContent: 'center',
            backgroundColor: 'white',
            borderRadius: '90%',
            height: 120,
            width: 120,
            marginTop: '10%',
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
        <Text category="h6">{post?.ride?.user?.firstName} {post?.ride?.user?.lastName}</Text>
      </View>
      <View style={{ marginTop: -40 }}>
        <MapView style={styles.map} region={initRegion}>
          <Marker coordinate={startLocation} />
          <Marker coordinate={endLocation} pinColor={'blue'} />
          <MapViewDirections
            origin={startLocation}
            destination={endLocation}
            apikey={'AIzaSyCwW5sS2iPAJt64l1f-sVJEU_yQZrySKYI'}
            strokeWidth={3}
            strokeColor="hotpink"
          />
        </MapView>
      </View>
      <View
        style={{
          marginTop: -120,
          height: 100,
          flexDirection: 'column',
          justifyContent: 'space-around',
        }}
      >
        <Text category="h6"> Trip Details: </Text>
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
          <Text> {capacity} </Text>
        </Layout>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  map: {
    width: '100%',
    height: '50%',
    marginTop: '10%',
  },
});

export default RidePostDetails;
