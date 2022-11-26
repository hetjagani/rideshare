import React from 'react';
import Home from '../screens/Home';
import Payment from '../screens/Payment';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Icon } from '@ui-kitten/components';
import {
  HOME_SCREEN,
  PAYMENT_SCREEN,
  HOME_NAVIGATOR,
  PROFILE_SCREEN,
  USER_INFO_SCREEN,
  ROOM_SCREEN,
  MESSAGE_SCREEN,
  ROOM_NAVIGATOR,
  MY_POSTS_SCREEN,
  RIDE_POST_DETAILS,
  PROFILE_NAVIGATOR,
  MY_RIDES_NAVIGATOR,
  MY_RIDES_BY_YOU_SCREEN,
  MY_RIDES_FOR_YOU_SCREEN,
  MY_RATINGS_NAVIGATOR,
  ADD_POST_SCREEN,
  ADD_POST_RIDE_SCREEN,
} from './AppRoutes';
import Profile from '../screens/Profile';
import { UserInfo } from '../screens/UserInfo';
import Room from '../screens/Room';
import Message from '../screens/Message';
import RidePostDetails from '../screens/RidePostDetails';
import MyPosts from '../screens/MyPosts';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import { RidesByYou, RidesForYou, TopTabBar } from '../screens/MyRides';
import MyRatings from '../screens/MyRatings';
import AddPost from '../screens/AddPost';
import AddPostRide from '../screens/AddPostRide';

const MyRidesTabs = createMaterialTopTabNavigator();

const Tab = createBottomTabNavigator();
const Stack = createStackNavigator();

const unfocusedColor = '#aaaaaa';
const focusedColor = '#000000';

const tabIcon = (name) => {
  return ({ focused }) => (
    <Icon
      name={name}
      fill={focused ? focusedColor : unfocusedColor}
      height={24}
      width={24}
    />
  );
};

const MyRidesNavigator = () => (
  <MyRidesTabs.Navigator tabBar={(props) => <TopTabBar {...props} />}>
    <MyRidesTabs.Screen name={MY_RIDES_BY_YOU_SCREEN} component={RidesByYou} />
    <MyRidesTabs.Screen
      name={MY_RIDES_FOR_YOU_SCREEN}
      component={RidesForYou}
    />
  </MyRidesTabs.Navigator>
);

const HomeNavigator = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name={HOME_SCREEN}
        component={Home}
        options={{ headerShown: false, title: 'Your Feed' }}
      />
      <Stack.Screen
        name={PAYMENT_SCREEN}
        component={Payment}
        options={{ headerShown: true, title: 'Payment Screen' }}
      />
      <Stack.Screen
        name={USER_INFO_SCREEN}
        component={UserInfo}
        options={{ headerShown: true, title: 'User Info Screen' }}
      />
      <Stack.Screen
        name={RIDE_POST_DETAILS}
        component={RidePostDetails}
        options={{ headerShown: true, title: 'Ride Details' }}
      />
      <Stack.Screen
        name={MY_RIDES_NAVIGATOR}
        component={MyRidesNavigator}
        options={{ headerShown: true, title: 'My Rides' }}
      />
      <Stack.Screen
        name={ADD_POST_SCREEN}
        component={AddPost}
        options={{ headerShown: true, title: 'Add Post' }}
      />
      <Stack.Screen
        name={ADD_POST_RIDE_SCREEN}
        component={AddPostRide}
        options={{ headerShown: true, title: 'Add Ride' }}
      />
    </Stack.Navigator>
  );
};

const ProfileNavigator = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name={PROFILE_SCREEN}
        component={Profile}
        options={{ headerShown: true, title: 'Profile Info' }}
      />
      <Stack.Screen
        name={MY_POSTS_SCREEN}
        component={MyPosts}
        options={{ headerShown: true, title: 'Your Posts' }}
      />
      <Stack.Screen
        name={USER_INFO_SCREEN}
        component={UserInfo}
        options={{ headerShown: true, title: 'User Info Screen' }}
      />
      <Stack.Screen
        name={MY_RATINGS_NAVIGATOR}
        component={MyRatings}
        options={{ headerShown: true, title: 'Your Ratings' }}
      />
    </Stack.Navigator>
  );
};

const RoomNavigator = ({ navigation }) => (
  <Stack.Navigator>
    <Stack.Screen
      name={ROOM_SCREEN}
      component={Room}
      options={{ headerShown: true, title: 'Chats' }}
    />
    <Stack.Screen
      name={MESSAGE_SCREEN}
      component={Message}
      options={({ route }) => ({
        tabBarVisible: false,
        title: route.params.userName,
        headerShown: true,
      })}
    />
  </Stack.Navigator>
);

export const AppStack = () => {
  return (
    <Tab.Navigator>
      <Tab.Screen
        name={HOME_NAVIGATOR}
        component={HomeNavigator}
        options={{
          headerShown: false,
          tabBarIcon: tabIcon('home'),
          title: 'Home',
        }}
      />
      <Tab.Screen
        name={ROOM_NAVIGATOR}
        component={RoomNavigator}
        options={({ route }) => ({
          headerShown: false,
          title: 'Chats',
          tabBarIcon: tabIcon('message-circle'),
        })}
      />
      <Tab.Screen
        name={PROFILE_NAVIGATOR}
        component={ProfileNavigator}
        options={{
          headerShown: false,
          title: 'Profile',
          tabBarIcon: tabIcon('person'),
        }}
      />
    </Tab.Navigator>
  );
};
