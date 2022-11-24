import React, { useEffect, useState } from 'react';
import { TabBar, Tab, Layout, Text, Card, List } from '@ui-kitten/components';
import { Rating } from 'react-native-ratings';
import fetchRatingsForYou from '../services/fetchRatingsForYou';
import { StyleSheet, View } from 'react-native';
import getAuthData from '../contexts/getAuthData';

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

const RatingsForYou = (props) => {
  const { user } = props;
  const [ratingsFor, setRatingsFor] = useState([]);

  const getRatingsForYou = async (userId) => {
    const result = await fetchRatingsForYou(userId);
    if (result?.response?.status == 401) {
      Toast.show({
        type: 'error',
        text1: 'Unauthorised',
      });
      return;
    }

    if (result?.response?.status == 500) {
      Toast.show({
        type: 'error',
        text1: 'Error Fetching Chats',
      });
      return;
    }

    setRatingsFor(result?.data?.nodes);
  };

  useEffect(() => {
    getAuthData((res) => {
      return res;
    })
      .then((user) => {
        getRatingsForYou(user.userId);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const renderHeader = (headerProps, info) => (
    <View {...headerProps}>
      <Text category="h6">
        {info?.item?.ratingUser?.firstName +
          ' ' +
          info?.item?.ratingUser?.lastName}
      </Text>
    </View>
  );

  const renderItem = (info) => (
    <Card
      style={styles.item}
      status="primary"
      header={(headerProps) => renderHeader(headerProps, info)}
    >
      <View>
        <Text>{info?.item?.description}</Text>
      </View>
      <View style={{ marginTop: 10, alignItems: 'center' }}>
        <Rating
          readonly
          showReadOnlyText={false}
          type="custom"
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
      data={ratingsFor}
      renderItem={renderItem}
    />
  );
};

export default RatingsForYou;
