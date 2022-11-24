import axiosInstance from '../axiosInstance';

const fetchRatingsByYou = async (ratingUserId) =>
  axiosInstance('/ratings', {
    method: 'GET',
    params: { ratingUserId },
  })
    .then((response) => response)
    .catch((err) => err);

export default fetchRatingsByYou;
