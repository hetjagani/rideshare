import axiosInstance from '../axiosInstance';

export const addAddress = async (data) => {
  return axiosInstance('/addresses', {
    method: 'POST',
    data,
  })
    .then((res) => res)
    .catch((err) => err);
};
