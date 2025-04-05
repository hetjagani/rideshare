import React, { useEffect, useState } from 'react';
import { TabBar, Tab, Layout, Text, Card, List } from '@ui-kitten/components';
import { Rating } from 'react-native-ratings';
import fetchRatingsByYou from '../services/fetchRatingsByYou';
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

const RatingsByYou = (props) => {
  const { ratingUser } = props;
  const [ratingsBy, setRatingsBy] = useState([]);

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
    setRatingsBy(res?.data?.nodes);
  };

  useEffect(() => {
    getAuthData((res) => {
      return res;
    })
      .then((user) => {
        getRatingsByYou(user.userId);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const renderHeaderBy = (headerProps, info) => (
    <View {...headerProps}>
      <Text category='h6'>
        {info?.item?.user?.firstName + ' ' + info?.item?.user?.lastName}
      </Text>
    </View>
  );

  const renderItemBy = (info) => (
    <Card
      style={styles.item}
      status="primary"
      header={(headerProps) => renderHeaderBy(headerProps, info)}
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
      data={ratingsBy}
      renderItem={renderItemBy}
    />
  );
};

export default RatingsByYou;
