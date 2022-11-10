import { Layout, Text, Menu, MenuItem, Icon } from '@ui-kitten/components';
import { globalStyles } from '../GlobalStyles';
import React from 'react';
import { View, Image, StyleSheet } from 'react-native';
import { ProfileMenuItems } from './ProfileMenuItems';

const Profile = ({navigation}) => {
  return (
    <View style={{ height: '100%', backgroundColor: 'white' }}>
      <Layout
        style={{ height: '40%', alignItems: 'center', paddingTop: '10%' }}
      >
        <Layout
          style={{
            justifyContent: 'center',
            backgroundColor: 'red',
            borderRadius: '90%',
            height: 180,
            width: 180,
          }}
        >
          <Image
            source={require('../assets/img_avatar.png')}
            style={{ borderRadius: '90%', height: '100%', width: '100%' }}
          />
        </Layout>
        <Layout style={{ marginTop: '3%' }}>
          <Text style={{ fontWeight: 'bold', fontSize: '20px' }}>
            Akash Rupapara
          </Text>
        </Layout>
        <View
          style={{
            marginTop: '2%',
            flexDirection: 'row',
            alignItems: 'center',
            width: '80%',
          }}
        >
          <View style={{ flex: 1, height: 1, backgroundColor: 'black' }} />
        </View>
        <Layout
          style={{
            flexDirection: 'row',
            marginTop: '2%',
            justifyContent: 'space-around',
            width: '80%',
          }}
        >
          <Layout style={{ flexDirection: 'column', alignItems: 'center' }}>
            <Layout>
              <Text>50</Text>
            </Layout>
            <Layout>
              <Text>Rides</Text>
            </Layout>
          </Layout>
          <Layout style={{ flexDirection: 'column', alignItems: 'center' }}>
            <Layout>
              <Text>50</Text>
            </Layout>
            <Layout>
              <Text>Ratings</Text>
            </Layout>
          </Layout>
          <Layout style={{ flexDirection: 'column', alignItems: 'center' }}>
            <Layout>
              <Text>50</Text>
            </Layout>
            <Layout>
              <Text>Years</Text>
            </Layout>
          </Layout>
        </Layout>
      </Layout>
      <Layout style={{ height: '50%', marginTop: "15%" }}>
        <Layout style={{ marginTop: '5%', alignItems: 'center' }}>
          <Text style={{ fontWeight: 'bold' }}>Account Settings</Text>
        </Layout>
        <Layout
       >
          <ProfileMenuItems navigation={navigation}/>
        </Layout>
      </Layout>
    </View>
  );
};

export default Profile;
