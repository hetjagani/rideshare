import axiosInstance from "../axiosInstance";

const signIn = async (payload) =>
  axiosInstance("/auth/login", {
    method: "POST",
    data: payload,
  }).then((response) => response);

export default signIn;