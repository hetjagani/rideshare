import axiosInstance from "../axiosInstance";

const signUp = async (payload) =>
  axiosInstance("/auth/signup", {
    method: "POST",
    data: payload,
  }).then((response) => response)
  .catch((err) => err);

export default signUp;