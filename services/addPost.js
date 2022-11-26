import axiosInstance from '../axiosInstance';

export const addPost = async (data) => {
  return axiosInstance('/posts', {
    method: 'POST',
    data,
  })
    .then((res) => res)
    .catch((err) => err);
};
