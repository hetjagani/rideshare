import { Layout, Text, Menu, MenuItem, Icon } from '@ui-kitten/components';
import { globalStyles } from '../GlobalStyles';
import React, {useEffect} from 'react';
import { View, Image, StyleSheet } from 'react-native';
import { ProfileMenuItems } from './ProfileMenuItems';
import { fetchUserDetails } from '../services/fetchUserDetails';

const Profile = ({navigation}) => {
  const [name, setName] = React.useState('Temp Name');
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
    
    const userName = res?.data?.firstName + " " + res?.data?.lastName;
    setName(userName);
  };

  useEffect(() => {
    getUserDetails();
  }, [])
  
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
            {name}
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
          <ProfileMenuItems navigation={navigation} />
        </Layout>
      </Layout>
    </View>
  );
};

export default Profile;
