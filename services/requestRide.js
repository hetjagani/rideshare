import axiosInstance from '../axiosInstance';

export const requestRide = async (data) => {
  return axiosInstance('/rides/requests', {
    method: 'POST',
    data,
  })
    .then((res) => {
      return res;
    })
    .catch((err) => err);
};
