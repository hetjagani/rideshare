import axiosInstance from '../axiosInstance';

export default async () =>
  axiosInstance('/rides/my', {
    method: 'GET',
  })
    .then((res) => res.data)
    .catch((err) => err);
