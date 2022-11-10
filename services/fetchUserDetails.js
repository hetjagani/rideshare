import axiosInstance from '../axiosInstance';

export const fetchUserDetails = async () => {
  try{
    const response = await axiosInstance('/users/me', {
        method: 'GET',
     });
     return response;
    }catch(e){
        return err;
    }
};
