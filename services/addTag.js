import axiosInstance from '../axiosInstance';

export const addTag = async (name) => {
  return axiosInstance('/tags', {
    method: 'POST',
    data: { name },
  })
    .then((res) => res)
    .catch((err) => err);
};
