import axios from 'axios';
import Toast from 'react-native-toast-message';
import { BACKEND_URL } from './Config';
import AsyncStorage from '@react-native-async-storage/async-storage';

const axiosInstance = axios.create({
  baseURL: BACKEND_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.request.use(async (config) => {
  console.log('Request URL: ', config.url, 'Requst Params: ', config.params);
  try {
    const authDataSerialized = await AsyncStorage.getItem('@AuthData');
    if (authDataSerialized) {
      //If there are data, it's converted to an Object and the state is updated.
      const _authData = JSON.parse(authDataSerialized);
      config.headers.Authorization = _authData.data.token;
      console.log('Sending token: ', _authData.data.token);
    }
  } catch (e) {
    console.log(e);
  }
  // const token = getCookie('token');
  // if ((config.url === '/users/signup' || config.url === '/users/login')) {
  //   return config;
  // }
  // if (!token) {
  //   // eslint-disable-next-line no-undef
  //   window.location.href = '/login';
  // }
  // // eslint-disable-next-line no-param-reassign
  // config.headers.Authorization = token;

  return config;
});

axiosInstance.interceptors.response.use(
  (res) => res,
  (err) => {
    if (!err.response) {
      Toast.show({
        type: 'error',
        text1: 'Network Error!',
        text2: 'Please retry after sometime',
      });
    }
    if (err && err.response && err.response.data && err.response.data.message) {
      if (typeof err.response.data.message === 'string') {
        console.log(err.response.data)
        Toast.show({
          type: 'error',
          text1: 'Oops!',
          text2: err.response.data.message,
        });
      } else {
        console.log('Error: ', err.response.data.message);
      }
    }
    return err;
  }
);

export default axiosInstance;
