import axios from "axios";
import Toast from "react-native-toast-message";
import { BACKEND_URL } from "./Config";

const axiosInstance = axios.create({
  baseURL: BACKEND_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use((config) => {
  console.log("Request URL: ", config.url);
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
    console.log(err);
    if (!err.response) {
      Toast.show({
        type: "error",
        text1: "Network Error!",
        text2: "Please retry after sometime",
      });
    }
    // if (err && err.response && err.response.status === 401) {
    //   if (err.response.data && err.response.data.message) {
    //     console.log(err.response.data.message);
    //   }
    //   toast.error('Unauthorized request!');
    //   return;
    // }
    // if (err && err.response && err.response.status === 403) {
    //   if (err.response.data.message && typeof (err.response.data.message) === 'string') {
    //     toast.error(err.response.data.message);
    //   } else {
    //     toast.error('Forbidden request!');
    //   }
    //   return;
    // }
    // if (err && err.response && err.response.data && err.response.data.error) {
    //   if (typeof (err.response.data.message) === 'string') {
    //     toast.error(err.response.data.error);
    //   } else {
    //     toast.error('Forbidden request!');
    //   }
    //   return;
    // }
    if (err && err.response && err.response.data && err.response.data.message) {
      if (typeof err.response.data.message === "string") {
        Toast.show({
          type: "error",
          text1: "Oops!",
          text2: err.response.data.message,
        });
      } else {
        console.log(err.response.data.message);
      }
    }
  }
);

export default axiosInstance;
