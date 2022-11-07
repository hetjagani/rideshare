import React, { createContext, useState, useContext, useEffect } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import Toast from "react-native-toast-message";
import login from "../services/login";
import signUpReq from "../services/signup";

//Create the Auth Context with the data type specified
//and a empty object
const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [authData, setAuthData] = useState();

  //the AuthContext start with loading equals true
  //and stay like this, until the data be load from Async Storage
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadStorageData();
  }, []);

  async function loadStorageData() {
    try {
      //Try get the data from Async Storage
      const authDataSerialized = await AsyncStorage.getItem("@AuthData");
      if (authDataSerialized) {
        //If there are data, it's converted to an Object and the state is updated.
        const _authData = JSON.parse(authDataSerialized);
        setAuthData(_authData);
      }
    } catch (error) {
    } finally {
      //loading finished
      setLoading(false);
    }
  }

  const signIn = async (authData) => {
    try {
      const _authData = await login(authData);
      console.log(_authData)

      if (_authData?.response?.status == 401) {
        Toast.show({
          type: "error",
          text1: "Cannot login, please check the credentials",
        });
        return;
      }

      if (!_authData) return;

      //Set the data in the context, so the App can be notified
      //and send the user to the AuthStack
      setAuthData(_authData.data.token);

      //Persist the data in the Async Storage
      //to be recovered in the next user session.
      AsyncStorage.setItem("@AuthData", JSON.stringify(_authData));
    } catch (error) {
      console.log(error);
    }
  };

  const signUp = async (data) => {
    try {
      const _authData = await signUpReq(data);

      if (_authData?.response?.status == 500) {
        Toast.show({
          type: "error",
          text1: "Cannot signup, server error",
        });
        return;
      }

      if (!_authData) return;

      //Set the data in the context, so the App can be notified
      //and send the user to the AuthStack
      setAuthData(_authData.data.token);

      //Persist the data in the Async Storage
      //to be recovered in the next user session.
      AsyncStorage.setItem("@AuthData", JSON.stringify(_authData));
    } catch (error) {
      console.log(error);
    }
  };

  const signOut = async () => {
    //Remove data from context, so the App can be notified
    //and send the user to the AuthStack
    setAuthData(undefined);

    //Remove the data from Async Storage
    //to NOT be recoverede in next session.
    await AsyncStorage.removeItem("@AuthData");
  };

  return (
    //This component will be used to encapsulate the whole App,
    //so all components will have access to the Context
    <AuthContext.Provider value={{ authData, loading, signIn, signOut, signUp }}>
      {children}
    </AuthContext.Provider>
  );
};

//A simple hooks to facilitate the access to the AuthContext
// and permit components to subscribe to AuthContext updates
function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }

  return context;
}

export { AuthContext, AuthProvider, useAuth };
