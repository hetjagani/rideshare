import React from 'react';
import { TabBar, Tab, Layout, Text } from '@ui-kitten/components';

const RatingsByYou = () => (
  <Layout style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
    <Text category='h1'>ByYou</Text>
  </Layout>
);

const RatingsForYou = () => (
  <Layout style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
    <Text category='h1'>ForYou</Text>
  </Layout>
);

const TopTabBar = ({ navigation, state }) => (
  <TabBar
    selectedIndex={state.index}
    onSelect={(index) => navigation.navigate(state.routeNames[index])}
  >
    <Tab title='Ratings By You' />
    <Tab title='Ratings For You' />
  </TabBar>
);

export { RatingsByYou, RatingsForYou, TopTabBar };
