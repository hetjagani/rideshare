import React, { useEffect, useState } from 'react';
import { TabBar, Tab, Layout, Text, Card, List } from '@ui-kitten/components';
import { Rating } from 'react-native-ratings';
import getAuthData from '../contexts/getAuthData';
import fetchRatingsByYou from '../services/fetchRatingsByYou';
import fetchRatingsForYou from '../services/fetchRatingsForYou';
import { StyleSheet, View } from 'react-native';

const styles = StyleSheet.create({
  container: {
    maxHeight: '100%',
  },
  contentContainer: {
    paddingHorizontal: 8,
    paddingVertical: 4,
  },
  item: {
    marginVertical: 2,
  },
});

const RatingsByYou = () => {
  const [ratingUserId, setRatingUserId] = useState();
  const [ratings, setRatings] = useState([]);

  const getRatingsByYou = async (ratingUserId) => {
    const res = await fetchRatingsByYou(ratingUserId);
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
        text1: 'Error Fetching Chats',
      });
      return;
    }
    console.log(res?.data?.nodes);
    setRatings(res?.data?.nodes);
  };

  useEffect(() => {
    getAuthData().then((authData) => {
      setRatingUserId(authData.userId);
    });
    getRatingsByYou(ratingUserId);
  }, []);

  const renderHeader = (headerProps, info) => (
    <View {...headerProps}>
      <Text category='h6'>
        {info?.item?.user?.firstName + ' ' + info?.item?.user?.lastName}
      </Text>
    </View>
  );

  const renderItem = (info) => (
    <Card
      style={styles.item}
      status='primary'
      header={(headerProps) => renderHeader(headerProps, info)}
    >
      <View>
        <Text>{info?.item?.description}</Text>
      </View>
      <View style={{ marginTop: 10, alignItems: 'center' }}>
        <Rating
          readonly
          showReadOnlyText={false}
          type='custom'
          showRating
          ratingTextColor={'#000000'}
          fractions={1}
          ratingCount={5}
          startingValue={info?.item?.rating}
        />
      </View>
    </Card>
  );

  return (
    <List
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      data={ratings}
      renderItem={renderItem}
    />
  );
};

const RatingsForYou = () => {
  const [userId, setUserId] = useState();
  const [ratings, setRatings] = useState([]);

  const getRatingsForYou = async (userId) => {
    const res = await fetchRatingsForYou(userId);
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
        text1: 'Error Fetching Chats',
      });
      return;
    }
    setRatings(res?.data?.nodes);
  };

  useEffect(() => {
    getAuthData().then((authData) => {
      setUserId(authData.userId);
    });
    getRatingsForYou(userId);
  }, []);

  const renderHeader = (headerProps, info) => (
    <View {...headerProps}>
      <Text category='h6'>
        {info?.item?.ratingUser?.firstName +
          ' ' +
          info?.item?.ratingUser?.lastName}
      </Text>
    </View>
  );

  const renderItem = (info) => (
    <Card
      style={styles.item}
      status='primary'
      header={(headerProps) => renderHeader(headerProps, info)}
    >
      <View>
        <Text>{info?.item?.description}</Text>
      </View>
      <View style={{ marginTop: 10, alignItems: 'center' }}>
        <Rating
          readonly
          showReadOnlyText={false}
          type='custom'
          showRating
          ratingTextColor={'#000000'}
          fractions={1}
          ratingCount={5}
          startingValue={info?.item?.rating}
        />
      </View>
    </Card>
  );

  return (
    <List
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      data={ratings}
      renderItem={renderItem}
    />
  );
};

const TopTabBar = ({ navigation, state }) => (
  <TabBar
    selectedIndex={state.index}
    onSelect={(index) => navigation.navigate(state.routeNames[index])}
    style={{ height: 50 }}
  >
    <Tab title='Ratings By You' />
    <Tab title='Ratings For You' />
  </TabBar>
);

export { RatingsByYou, RatingsForYou, TopTabBar };
