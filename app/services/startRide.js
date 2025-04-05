import axiosInstance from '../axiosInstance';

const startRide = async (id) =>
  axiosInstance(`rides/${id}/start`, {
    method: 'PUT',
  })
    .then((response) => response)
    .catch((err) => err);

export default startRide;
