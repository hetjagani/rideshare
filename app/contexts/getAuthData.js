import AsyncStorage from '@react-native-async-storage/async-storage';
import jwtDecode from 'jwt-decode';
import Toast from 'react-native-toast-message';

export default async () => {
  const authDataSerialized = await AsyncStorage.getItem('@AuthData');
  if (authDataSerialized) {
    const authData = await JSON.parse(authDataSerialized);
    const token = authData.data.token;
    const decoded = await jwtDecode(token);

    if (!decoded) {
      Toast.show({
        type: 'error',
        text1: 'Cannot get auth data',
        text2: 'An unexpected error occurred. Please try again.',
      });
    }
    return {
      exp: decoded.exp,
      iat: decoded.iat,
      roles: decoded.roles.split(','),
      userId: decoded.sub,
    };
  }
};
