import axiosInstance from '../axiosInstance';

const fetchPosts = async ({ page, limit }) =>
  axiosInstance('/posts', {
    method: 'GET',
    params: { page, limit },
  })
    .then((response) => {
      return response.data;
    })
    .catch((err) => {
      return err;
    });

export default fetchPosts;
