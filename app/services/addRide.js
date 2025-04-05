import axiosInstance from '../axiosInstance';

export const addRide = async (data) => {
  return axiosInstance('/rides', {
    method: 'POST',
    data,
  })
    .then((res) => res)
    .catch((err) => err);
};
