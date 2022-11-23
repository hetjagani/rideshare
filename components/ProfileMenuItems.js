import { Layout, Menu, MenuItem, Icon } from '@ui-kitten/components';
import React, { useState } from 'react';
import { StyleSheet } from 'react-native';
import { useAuth } from '../contexts/AuthContext';
import {
  MY_POSTS_SCREEN,
  MY_RIDES_SCREEN,
  USER_INFO_SCREEN,
} from '../routes/AppRoutes';

export const ProfileMenuItems = ({ navigation, props }) => {
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const auth = useAuth();
  const signOut = () => {
    auth.signOut();
  };

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
        <MenuItem title="Update Role" accessoryRight={ForwardIcon} />
        <MenuItem
          title="My Posts"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(MY_POSTS_SCREEN)}
        />
        <MenuItem
          title="My Rides"
          accessoryRight={ForwardIcon}
          onPress={() => navigation.navigate(MY_RIDES_SCREEN)}
        />
        <MenuItem title="Sign Out" onPress={signOut} />
      </Menu>
    </Layout>
  );
};
