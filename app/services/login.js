import axiosInstance from "../axiosInstance";

const signIn = async (payload) =>
  axiosInstance("/auth/login", {
    method: "POST",
    data: payload,
  }).then((response) => response)
  .catch((err) => err);

export default signIn;