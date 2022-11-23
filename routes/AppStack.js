import React from "react";
import Home from "../screens/Home";
import Payment from "../screens/Payment";
import { createStackNavigator } from "@react-navigation/stack";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { Icon } from "@ui-kitten/components";
import {
  HOME_SCREEN,
  PAYMENT_SCREEN,
  HOME_NAVIGATOR,
  PROFILE_SCREEN,
  USER_INFO_SCREEN,
  ROOM_SCREEN,
  MESSAGE_SCREEN,
  ROOM_NAVIGATOR,
} from "./AppRoutes";
import Profile from "../screens/Profile";
import { UserInfo } from "../screens/UserInfo";
import Room from "../screens/Room";
import Message from "../screens/Message";

const { Navigator, Screen } = createStackNavigator();
import {
  RIDE_POST_DETAILS,
  MY_POSTS_SCREEN,
  PROFILE_NAVIGATOR,
} from "./AppRoutes";
import MyPosts from "../screens/MyPosts";
import { useNavigationState } from "@react-navigation/native";

const Tab = createBottomTabNavigator();
const Stack = createStackNavigator();

const unfocusedColor = "#aaaaaa";
const focusedColor = "#000000";

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

const HomeNavigator = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name={HOME_SCREEN}
        component={Home}
        options={{ headerShown: true, title: "Your Feed" }}
      />
      <Stack.Screen
        name={PAYMENT_SCREEN}
        component={Payment}
        options={{ headerShown: true, title: "Payment Screen" }}
      />
      <Stack.Screen
        name={USER_INFO_SCREEN}
        component={UserInfo}
        options={{ headerShown: true, title: "User Info Screen" }}
      />
    </Stack.Navigator>
  );
};

const RoomNavigator = ({navigation }) => (
  <Stack.Navigator>
    <Stack.Screen
      name={ROOM_SCREEN}
      component={Room}
      options={{ headerShown: true, title: "Chats" }}
    />
    <Stack.Screen
      name={MESSAGE_SCREEN}
      component={Message}
      options={({ route }) => ({
        tabBarVisible: false,
        title: route.params.userName,
        headerShown: true
      })}
    />
  </Stack.Navigator>
);
const ProfileNavigator = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name={PROFILE_SCREEN}
        component={Profile}
        options={{ headerShown: true, title: "Profile Info" }}
      />
      <Stack.Screen
        name={MY_POSTS_SCREEN}
        component={MyPosts}
        options={{ headerShown: true, title: "Your Posts" }}
      />
    </Stack.Navigator>
  );
};

export const AppStack = () => {
  return (
    <Tab.Navigator>
      <Tab.Screen
        name={HOME_NAVIGATOR}
        component={HomeNavigator}
        options={{
          headerShown: false,
          tabBarIcon: tabIcon("home"),
          title: "Home",
        }}
      />
      <Tab.Screen
        name={ROOM_NAVIGATOR}
        component={RoomNavigator}
        options={({route}) => ({
          headerShown: false,
          title: "Chats",
          tabBarIcon: tabIcon("message-circle"),
        })}
      />
      <Tab.Screen
        name={PROFILE_NAVIGATOR}
        component={ProfileNavigator}
        options={{
          headerShown: false,
          title: "Profile",
          tabBarIcon: tabIcon("person"),
        }}
      />
    </Tab.Navigator>
  );
};
