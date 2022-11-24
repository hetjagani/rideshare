import { TabBar, Tab, TabView, Text } from '@ui-kitten/components';
import React from 'react';
import { useState } from 'react';
import RatingsByYou from './RatingsByYou';
import RatingsForYou from './RatingsForYou';
import { useEffect } from 'react';
import getAuthData from '../contexts/getAuthData';

const MyRatings = () => {
  const [selectedIndex, setSelectedIndex] = useState(0);
  const shouldLoadComponent = (index) => index === selectedIndex;
  const [user, setUser] = useState();

  useEffect(() => {
    getAuthData().then((authData) => {
      setUser(authData.userId);
    });
  }, []);

  return (
    <TabView
      selectedIndex={selectedIndex}
      onSelect={(index) => setSelectedIndex(index)}
      shouldLoadComponent={shouldLoadComponent}
      style={{ height: '100%' }}
    >
      <Tab
        style={{ height: 42 }}
        children={<RatingsByYou ratingUser={user} />}
        title='Ratings By You'
      ></Tab>
      <Tab
        style={{ height: 42 }}
        children={<RatingsForYou user={user} />}
        title='Ratings For You'
      ></Tab>
    </TabView>
  );
};

export default MyRatings;
