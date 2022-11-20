import { Layout, Menu, MenuItem, Icon } from '@ui-kitten/components';
import React, { useState } from 'react';
import { StyleSheet } from 'react-native';
import { useAuth } from '../contexts/AuthContext';
import { USER_INFO_SCREEN } from '../routes/AppRoutes';

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
        <MenuItem title="Account Info" accessoryRight={ForwardIcon} onPress={() => navigation.navigate(USER_INFO_SCREEN)} />
        <MenuItem title="Update Role" accessoryRight={ForwardIcon} />
        <MenuItem title="Posts" accessoryRight={ForwardIcon} />
        <MenuItem title="Sign Out" onPress={signOut} />
      </Menu>
    </Layout>
  );
};
