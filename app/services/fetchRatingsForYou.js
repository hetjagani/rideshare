import axiosInstance from '../axiosInstance';

const fetchRatingsForYou = async (userId) =>
  axiosInstance('/ratings', {
    method: 'GET',
    params: { userId: userId },
  })
    .then((response) => response)
    .catch((err) => err);

export default fetchRatingsForYou;
