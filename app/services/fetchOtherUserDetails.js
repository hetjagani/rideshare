import axiosInstance from '../axiosInstance';

export const fetchOtherUserDetails = async (id) => {
  try {
    const response = await axiosInstance(`/users/${id}`, {
      method: 'GET',
    });
    return response?.data;
  } catch (e) {
    return err;
  }
};
