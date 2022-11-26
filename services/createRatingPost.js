import axiosInstance from '../axiosInstance';

const createRatingPost = async (payload) =>
  axiosInstance('/posts', {
    method: 'POST',
    data: payload,
  })
    .then((response) => response)
    .catch((err) => err);

export default createRatingPost;
