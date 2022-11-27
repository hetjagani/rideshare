import { Layout, Menu, MenuItem, Icon } from '@ui-kitten/components';
import React, { useState, useEffect } from 'react';
import { StyleSheet } from 'react-native';
import { useAuth } from '../contexts/AuthContext';
import {
  MY_POSTS_SCREEN,
  MY_RIDES_NAVIGATOR,
  USER_INFO_SCREEN,
  MY_RATINGS_NAVIGATOR,
  UPDATE_ROLE_SCREEN,
} from '../routes/AppRoutes';
import getAuthData from '../contexts/getAuthData';
import { useIsFocused } from '@react-navigation/native';

export const ProfileMenuItems = ({ navigation, isDriver }) => {
  const [selectedIndex, setSelectedIndex] = useState(-1);

  const auth = useAuth();
  const signOut = () => {
    auth.signOut();
  };

  const isFocused = useIsFocused();

  const styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      justifyContent: 'space-between',
    },
    menu: {
      flex: 1,
      margin: 8,
    },
  });

  const ForwardIcon = (props) => <Icon {...props} name="arrow-ios-forward" />;
  return (
    <Layout style={styles.container} level="1">
      <Menu
        style={styles.menu}
        selectedIndex={selectedIndex}
        onSelect={(index) => setSelectedIndex(index)}
      >
        <MenuItem
          title="Account Info"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(USER_INFO_SCREEN)}
        />
        {isDriver === false ? (
          <MenuItem
            title="Become Driver"
            accessoryRight={ForwardIcon}
            onPress={() => navigation.navigate(UPDATE_ROLE_SCREEN)}
          />
        ) : null}
        <MenuItem
          title="My Posts"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(MY_POSTS_SCREEN)}
        />
        <MenuItem
          title="My Rides"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(MY_RIDES_NAVIGATOR)}
        />
        <MenuItem
          title="My Ratings"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(MY_RATINGS_NAVIGATOR)}
        />
        <MenuItem title="Sign Out" onPress={signOut} />
      </Menu>
    </Layout>
  );
};
