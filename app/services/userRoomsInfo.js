import axiosInstance from "../axiosInstance";

const userRoomsInfo = async () =>
  axiosInstance("/rooms/users", {
    method: "GET",
  }).then((response) => response)
  .catch((err) => err);

export default userRoomsInfo;