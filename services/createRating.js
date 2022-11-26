import axiosInstance from '../axiosInstance';

const createRating = async (payload, rideId) =>
  axiosInstance('/ratings', {
    method: 'POST',
    data: payload,
  })
    .then((response) => {
      axiosInstance(`/rides/${rideId}/ratings/${response?.data?.id}`, {
        method: 'POST',
      })
        .then((response) => response)
        .catch((err) => err);
      return response;
    })
    .catch((err) => err);

export default createRating;
