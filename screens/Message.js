import { Icon } from "@ui-kitten/components";
import React, { useEffect, useState, useCallback } from "react";
import { View } from "react-native";
import { GiftedChat, Bubble, Send } from "react-native-gifted-chat";
import getMessages from "../services/getMessages";
import { BACKEND_URL } from "../Config";
import { useSafeAreaInsets } from "react-native-safe-area-context";

var stompClient = null;

const Message = ({ route }) => {
  const { roomId, senderId, receiverId, userName } = route.params;
  const [error, setError] = useState("");
  const [clientConnection, setClientConnection] = useState(false);
  const [messages, setMessages] = useState([]);
  const insets = useSafeAreaInsets();

  const mapMessages = (msgs) => {
    return msgs.map((ele) => {
      return {
        _id: ele.id,
        text: ele.content,
        createdAt: new Date(ele.createdAt),
        user: {
          _id: String(ele.senderId),
          name: userName,
        },
      };
    });
  };

  const existing = async (roomId) => {
    try {
      const msgs = await getMessages(roomId);
      if (msgs?.response?.status == 401) {
        Toast.show({
          type: "error",
          text1: "Unauthorised",
        });
        return;
      }

      if (msgs?.response?.status == 500) {
        Toast.show({
          type: "error",
          text1: "Error Fetching Chats",
        });
        return;
      }

      const m = mapMessages(msgs?.data?.nodes);

      setMessages(m);
    } catch (error) {
      console.log(error);
    }
  };

  let socket;
  const connect = () => {
    const Stomp = require("stompjs");
    var SockJS = require("sockjs-client");
    socket = new SockJS(`${BACKEND_URL}/ws`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    const path = `/user/${senderId}/queue/messages`;
    stompClient.subscribe(path, onMessageReceived);
  };

  const onMessageReceived = (message) => {
    const msg = JSON.parse(message.body);

    const m = mapMessages([msg]);
    setMessages((previousMessages) =>
      GiftedChat.append(previousMessages, m[0])
    );
  };

  const onError = () => {
    console.log("error");
  };

  useEffect(() => {
    if (clientConnection == false) {
      connect();
      setClientConnection(true);
    }
    existing(roomId);
  }, []);

  const onSend = useCallback((messages = []) => {
    const mess = {
      roomId: roomId,
      content: messages[0].text,
      senderId: senderId,
      receiverId: receiverId,
    };
    stompClient.send("/app/chat", {}, JSON.stringify(mess));

    setMessages((previousMessages) =>
      GiftedChat.append(previousMessages, messages)
    );
  }, []);

  const renderBubble = (props) => {
    return (
      <Bubble
        {...props}
        wrapperStyle={{
          right: {
            backgroundColor: "#2e64e5",
          },
        }}
        textStyle={{
          right: {
            color: "#fff",
          },
        }}
      />
    );
  };

  const renderSend = (props) => {
    return (
      <Send {...props}>
        <View>
          <Icon
            name="arrow-circle-up"
            fill="#2e64e5"
            height={32}
            width={32}
            style={{ marginBottom: 5, marginRight: 5 }}
          />
        </View>
        {/* <View>
          <Icon
            name={"arrow-upward"}
            // height={32}
            // width={32}
            // color="#2e64e5"
            // style={{ marginBottom: '5', marginRight: '5' }}
          />
        </View> */}
      </Send>
    );
  };

  return (
    <GiftedChat
      messages={messages}
      onSend={(messages) => onSend(messages)}
      user={{
        _id: String(senderId),
      }}
      wrapInSafeArea={false}
      bottomOffset={insets.bottom}
      renderBubble={renderBubble}
      alwaysShowSend
      renderSend={renderSend}
      placeholder="Type your message here..."
    />
  );
};

export default Message;
