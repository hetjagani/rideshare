import axiosInstance from '../axiosInstance';

const verifyDocument = async (data) =>
  axiosInstance('/documents', {
    method: 'POST',
    data,
  })
    .then((response) => {
      return response;
    })
    .catch((err) => {
      console.log(err);
    });

export default verifyDocument;
