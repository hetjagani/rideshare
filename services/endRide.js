import axiosInstance from '../axiosInstance';

const endRide = async (id) =>
  axiosInstance(`rides/${id}/stop`, {
    method: 'PUT',
  })
    .then((response) => response)
    .catch((err) => err);

export default endRide;
