import axiosInstance from '../axiosInstance';

export default fetchPosts = async ({ page, limit, userId }) =>
  axiosInstance('/posts', {
    method: 'GET',
    params: { page, limit, userId },
  })
    .then((response) => {
      return response.data;
    })
    .catch((err) => {
      return err;
    });
