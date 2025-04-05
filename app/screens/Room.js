import { Layout, Text } from '@ui-kitten/components';
import React, { useEffect, useState } from 'react';
import {
  View,
  Image,
  TouchableOpacity,
  InteractionManager,
} from 'react-native';
import userRoomsInfo from '../services/userRoomsInfo';
import { set } from 'react-hook-form';

import { FlatList } from 'react-native-gesture-handler';
import { MESSAGE_SCREEN } from '../routes/AppRoutes';
import { ChatStyles } from '../styles/ChatStyles';
import getAuthData from '../contexts/getAuthData';
import { useIsFocused } from '@react-navigation/native';

const Room = ({ navigation }) => {
  const [rooms, setRooms] = useState([]);
  const [currentUser, setCurrentUser] = useState();
  const isFocused = useIsFocused();

  const getRooms = async () => {
    const res = await userRoomsInfo();
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
    setRooms(res?.data?.nodes);
  };

  useEffect(() => {
    getRooms();
    getAuthData().then((authData) => {
      setCurrentUser(authData.userId);
    });
  }, [isFocused]);

  return (
    <View style={ChatStyles.container}>
      {rooms?.length <= 0 && (
        <View>
          <Text>No Chats exist</Text>
        </View>
      )}
      {rooms?.length > 0 && (
        <FlatList
          data={rooms}
          keyExtractor={(item) => item.roomId}
          renderItem={({ item }) => (
            <TouchableOpacity
              style={ChatStyles.card}
              onPress={() =>
                navigation.navigate(MESSAGE_SCREEN, {
                  userName: item.name,
                  roomId: item.roomId,
                  senderId: currentUser,
                  receiverId: item.userId,
                })
              }
            >
              <View style={ChatStyles.userinfo}>
                <View style={ChatStyles.userimgwrapper}>
                  <Image
                    style={ChatStyles.userimg}
                    source={item.profileImage != "" ? { uri: item.profileImage } : require('../assets/img_avatar.png')}
                  />
                </View>
                <View style={ChatStyles.textsection}>
                  <View style={ChatStyles.userinfotext}>
                    <Text style={ChatStyles.username}>{item.name}</Text>
                    <Text style={ChatStyles.posttime}>
                      {item.createdAt == 0
                        ? ''
                        : new Date(item.createdAt).toLocaleString('en-US')}
                    </Text>
                  </View>
                  <Text style={ChatStyles.messagetext}>{item.last}</Text>
                </View>
              </View>
            </TouchableOpacity>
          )}
        />
      )}
    </View>
  );
};

export default Room;
