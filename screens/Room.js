import { Layout, Text } from "@ui-kitten/components";
import React, { useEffect, useState } from "react";
import {
  View,
  Image,
  TouchableOpacity,
  InteractionManager,
} from "react-native";
import userRoomsInfo from "../services/userRoomsInfo";
import { set } from "react-hook-form";

import { FlatList } from "react-native-gesture-handler";
import { MESSAGE_SCREEN } from "../routes/AppRoutes";
import { ChatStyles } from "../styles/ChatStyles";

const Messages = [
  {
    id: "1",
    userName: "Jenny Doe",
    userImg: require("../assets/splash.png"),
    messageTime: "4 mins ago",
    messageText:
      "Hey there, this is my test for a post of my social app in React Native.",
  },
  {
    id: "2",
    userName: "John Doe",
    userImg: require("../assets/splash.png"),
    messageTime: "2 hours ago",
    messageText:
      "Hey there, this is my test for a post of my social app in React Native.",
  },
  {
    id: "3",
    userName: "Ken William",
    userImg: require("../assets/splash.png"),
    messageTime: "1 hours ago",
    messageText:
      "Hey there, this is my test for a post of my social app in React Native.",
  },
  {
    id: "4",
    userName: "Selina Paul",
    userImg: require("../assets/splash.png"),
    messageTime: "1 day ago",
    messageText:
      "Hey there, this is my test for a post of my social app in React Native.",
  },
  {
    id: "5",
    userName: "Christy Alex",
    userImg: require("../assets/splash.png"),
    messageTime: "2 days ago",
    messageText:
      "Hey there, this is my test for a post of my social app in React Native.",
  },
];

const Room = ({ navigation }) => {
  const [rooms, setRooms] = useState([]);
  
  const getRooms = async () => {
    const res = await userRoomsInfo();
    console.log(res.data);
    if (res?.response?.status == 401) {
      Toast.show({
        type: "error",
        text1: "Unauthorised",
      });
      return;
    }

    if (res?.response?.status == 500) {
      Toast.show({
        type: "error",
        text1: "Error Fetching Chats",
      });
      return;
    }
    setRooms(res?.data?.nodes);
  };

  useEffect(() => {
    getRooms();
  }, []);

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
                  userName: item.userName,
                  roomId: item.roomId,
                })
              }
            >
              <View style={ChatStyles.userinfo}>
                <View style={ChatStyles.userimgwrapper}>
                  <Image
                    style={ChatStyles.userimg}
                    source={{uri:item.profileImage}}
                  />
                </View>
                <View style={ChatStyles.textsection}>
                  <View style={ChatStyles.userinfotext}>
                    <Text style={ChatStyles.username}>{item.name}</Text>
                    <Text style={ChatStyles.posttime}>{item.createdAt == 0 ? "": new Date(item.createdAt).toLocaleString('en-US')}</Text>
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
