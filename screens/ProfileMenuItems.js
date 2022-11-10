import { Layout, Menu, MenuItem, Icon } from '@ui-kitten/components';
import React, { useState } from 'react';
import { View, Image, StyleSheet } from 'react-native';
import { UserInfo } from './UserInfo';
import { createStackNavigator, createAppContainer } from 'react-navigation';  


export const ProfileMenuItems = ({navigation}) => {
  const [selectedIndex, setSelectedIndex] = useState(-1);

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
        <MenuItem title="Account Info" accessoryRight={ForwardIcon} onPress={()=> navigation.navigate("USER_INFO_SCREEN")}/>
        <MenuItem title="Update Role" accessoryRight={ForwardIcon} />
        <MenuItem title="Posts" accessoryRight={ForwardIcon} />
      </Menu>
    </Layout>
  );
};
