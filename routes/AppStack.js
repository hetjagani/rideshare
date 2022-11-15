import React from "react";
import Home from "../screens/Home";
import Payment from "../screens/Payment";
import { createStackNavigator } from "@react-navigation/stack";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { Icon } from "@ui-kitten/components";
import { HOME_SCREEN, PAYMENT_SCREEN, HOME_NAVIGATOR, PROFILE_SCREEN, USER_INFO_SCREEN } from "./AppRoutes";
import Profile from "../screens/Profile";
import { UserInfo } from "../screens/UserInfo";
const { Navigator, Screen } = createStackNavigator();


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
        options={{ headerShown: true, title:"Home Screen" }}
      />
      <Stack.Screen
        name={PAYMENT_SCREEN}
        component={Payment}
        options={{ headerShown: true, title:"Payment Screen" }}
      />
      <Stack.Screen 
        name={USER_INFO_SCREEN}
        component={UserInfo}
        options={{ headerShown: true, title:"User Info Screen" }}
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
          title: "Home"
        }}
      />
      <Tab.Screen 
        name={PROFILE_SCREEN}
        component={Profile}
        options={{
          headerShown: true,
          title: "Profile",
          tabBarIcon: tabIcon("person")
        }}
      />
    </Tab.Navigator>
  );
};
