import axiosInstance from '../axiosInstance';

const fetchRatingById = async (ratingsId) =>
  axiosInstance(`/ratings/${ratingsId}`, {
    method: 'GET',
  })
    .then((response) => response)
    .catch((err) => err);

export default fetchRatingById;
