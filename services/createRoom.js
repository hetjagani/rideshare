import axiosInstance from '../axiosInstance';

const createRoom = async (payload, rideId) =>
  axiosInstance('/rooms', {
    method: 'POST',
    data: payload,
  })
    .then((response) => response?.data)
    .catch((err) => err);

export default createRoom;
