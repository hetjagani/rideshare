import axiosInstance from '../axiosInstance';

const likePost = async (id) => {
  return axiosInstance(`posts/${id}/like`, {
    method: 'PUT',
  })
    .then((res) => res.data)
    .catch((err) => err);
};

const dislikePost = async (id) => {
  return axiosInstance(`posts/${id}/dislike`, {
    method: 'PUT',
  })
    .then((res) => res.data)
    .catch((err) => err);
};

export { likePost, dislikePost };
