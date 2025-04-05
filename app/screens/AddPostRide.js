import {
  Autocomplete,
  AutocompleteItem,
  Button,
  Icon,
  Input,
  Layout,
  Text,
} from '@ui-kitten/components';
import axios from 'axios';
import React, { useEffect, useState } from 'react';
import {
  Dimensions,
  KeyboardAvoidingView,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  View,
} from 'react-native';
import { RADAR_API_SECRET, RADAR_API_URL } from '../Config';
import { addAddress } from '../services/addAddress';
import DatePicker from 'react-native-modern-datepicker';
import Tags from 'react-native-tags';
import Pill from '../components/Pill';
import { addTag } from '../services/addTag';
import { addRide } from '../services/addRide';
import Toast from 'react-native-toast-message';
import { ADD_POST_SCREEN } from '../routes/AppRoutes';

const AddPostRide = ({ navigation }) => {
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

  const [startAddresses, setStartAddresses] = useState([]);
  const [endAddresses, setEndAddresses] = useState([]);
  const [startAddress, setStartAddress] = useState({});
  const [endAddress, setEndAddress] = useState({});
  const [startAddressBE, setStartAddressBE] = useState({});
  const [endAddressBE, setEndAddressBE] = useState({});

  const [noPassengers, setNoPassengers] = useState(0);
  const [pricePerPerson, setPricePerPerson] = useState(0.0);
  const [rideTime, setRideTime] = useState(new Date());
  const [tags, setTags] = useState([]);
  const [tagIds, setTagIds] = useState([]);

  const [showDatePicker, setShowDatePicker] = useState(false);

  const getAddresses = async (query) => {
    return axios.get(RADAR_API_URL, {
      headers: {
        Authorization: RADAR_API_SECRET,
      },
      params: {
        query,
      },
    });
  };

  const onStartAddressSelect = (index) => {
    const sa = startAddresses[index];
    setStartAddress(startAddresses[index].formattedAddress);
    const beAddress = {
      street: sa?.addressLabel == '' ? sa?.street : sa?.addressLabel,
      line: sa?.neighborhood,
      city: sa?.city,
      state: sa?.state,
      country: sa?.country,
      zipcode: sa?.postalCode,
      latitude: sa?.latitude,
      longitude: sa?.longitude,
    };
    setStartAddressBE(beAddress);
  };

  const onEndAddressSelect = (index) => {
    const sa = endAddresses[index];
    setEndAddress(endAddresses[index].formattedAddress);
    const beAddress = {
      street: sa?.addressLabel == '' ? sa?.street : sa?.addressLabel,
      line: sa?.neighborhood,
      city: sa?.city,
      state: sa?.state,
      country: sa?.country,
      zipcode: sa?.postalCode,
      latitude: sa?.latitude,
      longitude: sa?.longitude,
    };
    setEndAddressBE(beAddress);
  };

  const onStartChangeText = (query) => {
    setStartAddress(query);
    // query api and set addresses t that
    getAddresses(query).then((res) => {
      setStartAddresses(res?.data?.addresses);
    });
  };

  const onEndChangeText = (query) => {
    setEndAddress(query);
    // query api and set addresses t that
    getAddresses(query).then((res) => {
      setEndAddresses(res?.data?.addresses);
    });
  };

  const renderOption = (item, index) => (
    <AutocompleteItem key={index} title={item.formattedAddress} />
  );

  const createStartAddressBE = () => {
    addAddress(startAddressBE).then((res) => {
      setStartAddressBE(res?.data);
    });
  };

  const createStopAddressBE = () => {
    addAddress(endAddressBE).then((res) => {
      setEndAddressBE(res?.data);
    });
  };

  const startAddressSaveIcon = (props) => {
    return (
      <TouchableOpacity onPress={() => createStartAddressBE()}>
        <Icon name={'save-outline'} {...props} />
      </TouchableOpacity>
    );
  };

  const stopAddressSaveIcon = (props) => {
    return (
      <TouchableOpacity onPress={() => createStopAddressBE()}>
        <Icon name={'save-outline'} {...props} />
      </TouchableOpacity>
    );
  };

  const dollarSign = (props) => {
    return <Text category="s1">$</Text>;
  };

  const toggleDatePicker = () => {
    setShowDatePicker(!showDatePicker);
  };

  const onTagsChange = async (tags) => {
    setTagIds([]);
    const promises = [];
    tags.forEach((t) => {
      promises.push(addTag(t));
    });

    Promise.all(promises).then((responses) => {
      const tIds = [];
      const ts = [];
      responses.forEach((res) => {
        res?.data?.id && tIds.push(res?.data?.id);
        res?.data?.name && ts.push(res?.data?.name);
      });

      setTagIds(tIds);
      setTags(ts);
    });
  };

  const saveRide = () => {
    const data = {
      startAddress: startAddressBE?.id,
      endAddress: endAddressBE?.id,
      noPassengers,
      pricePerPerson,
      rideTime: new Date(rideTime).getTime(),
      tagIds,
    };

    addRide(data).then((res) => {
      if (res?.response && res?.response.status != 200) {
        console.log(res?.response);
        Toast.show({
          type: 'error',
          text1: 'Error Adding Ride',
          text2: res?.response?.data?.message,
        });
        return;
      }

      navigation.navigate(ADD_POST_SCREEN, {
        refId: res?.data?.id,
        ride: res?.data,
      });
    });
  };

  return (
    <KeyboardAvoidingView
      behavior="padding"
      style={{ height: '100%', backgroundColor: 'white' }}
    >
      <ScrollView style={{ backgroundColor: 'white' }}>
        <Layout style={styles.container}>
          <Text category="h1">Ride Details</Text>
          <View style={styles.rideForm}>
            <Autocomplete
              label={'Start Address'}
              placeholder="Place your Text"
              value={startAddress}
              onSelect={onStartAddressSelect}
              onChangeText={onStartChangeText}
              style={styles.rideInput}
              accessoryRight={startAddressSaveIcon}
            >
              {startAddresses.map(renderOption)}
            </Autocomplete>
            {startAddressBE?.id && (
              <View style={styles.rideInput}>
                <Text category="s1">Start Address: </Text>
                <Text category="s2">
                  {startAddressBE?.street}, {startAddressBE?.line},{' '}
                  {startAddressBE?.city}, {startAddressBE?.country},{' '}
                  {startAddressBE?.zipcode}
                </Text>
              </View>
            )}

            <Autocomplete
              label={'End Address'}
              placeholder="Place your Text"
              value={endAddress}
              onSelect={onEndAddressSelect}
              onChangeText={onEndChangeText}
              style={styles.rideInput}
              accessoryRight={stopAddressSaveIcon}
            >
              {endAddresses.map(renderOption)}
            </Autocomplete>
            {endAddressBE?.id && (
              <View style={styles.rideInput}>
                <Text category="s1">End Address: </Text>
                <Text category="s2">
                  {endAddressBE?.street}, {endAddressBE?.line},{' '}
                  {endAddressBE?.city}, {endAddressBE?.country},{' '}
                  {endAddressBE?.zipcode}
                </Text>
              </View>
            )}

            <Input
              label={'No of Passengers'}
              onChangeText={(next) => setNoPassengers(next)}
              style={styles.rideInput}
              value={noPassengers}
              keyboardType="numeric"
            />

            <Input
              label={'Price Per Person'}
              onChangeText={(next) => setPricePerPerson(next)}
              style={styles.rideInput}
              value={pricePerPerson}
              keyboardType="numeric"
              accessoryLeft={dollarSign}
            />

            <View style={styles.rideInput}>
              <Button onPress={toggleDatePicker}>
                {showDatePicker == false
                  ? 'Select Ride Time'
                  : 'Hide Date Picker'}
              </Button>
              {showDatePicker && (
                <DatePicker
                  onSelectedChange={(date) => {
                    setRideTime(date);
                  }}
                />
              )}
              <Text category="s1">Ride Time: </Text>
              <Text category="s2">
                {new Date(rideTime).toDateString()}
                {', '}
                {new Date(rideTime).toLocaleTimeString()}
              </Text>
            </View>

            <View style={styles.rideInput}>
              <Text category="c1" style={{ fontWeight: 'bold', color: '#777' }}>
                Tags
              </Text>
              <Tags
                initialTags={tags}
                deleteTagOnPress={true}
                textInputProps={{
                  placeholder: 'Add Tags Here',
                }}
                onChangeTags={onTagsChange}
                containerStyle={{ justifyContent: 'center' }}
                inputStyle={{
                  backgroundColor: 'white',
                  borderColor: 'blue',
                  borderRadius: 10,
                  borderWidth: 1,
                  color: 'blue',
                  fontWeight: 'bold',
                  fontSize: '16',
                }}
                renderTag={({ tag, index, onPress }) => (
                  <TouchableOpacity key={`${tag}-${index}`} onPress={onPress}>
                    <Pill type="NORMAL" text={tag} key={index} />
                  </TouchableOpacity>
                )}
              />
            </View>
            <View style={styles.rideInput}>
              <Button onPress={() => saveRide()}>Save</Button>
            </View>
          </View>
        </Layout>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

export default AddPostRide;
