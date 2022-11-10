import { Text, Input, Layout, Modal, Card, Button } from '@ui-kitten/components';
import React from 'react';
import {
  View,
  Image,
  StyleSheet,
  KeyboardAvoidingView,
  ScrollView,
} from 'react-native';

export const UserInfo = () => {
  const [email, setEmail] = React.useState('');
  const [firstName, setFirstName] = React.useState('');
  const [lastName, setLastName] = React.useState('');
  const [contactNo, setContactNo] = React.useState('');
  const [visible, setVisible] = React.useState(false);

  const styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
    },
    input: {
      flex: 1,
      margin: 2,
    },
  });

  return (
    <KeyboardAvoidingView enabled behavior="padding">
      <View
        style={{
          height: '100%',
          width: '100%',
          alignItems: 'center',
          backgroundColor: 'white',
        }}
      >
        {/* <Layout
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
        <Layout>
          <Button title={'Change Profile Pic'} />
        </Layout> */}
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
        <Button style={{marginTop: "2%"}} onPress={() => setVisible(true)}> Change Profile Pic </Button>

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
