import axiosInstance from "../axiosInstance";

const getMessages = async (roomId) =>
  axiosInstance(`/rooms/${roomId}/messages`, {
    method: "GET",
  }).then((response) => response)
  .catch((err) => err);

export default getMessages;