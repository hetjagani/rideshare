import axiosInstance from "../axiosInstance";

const fetchPaymentSheetParams = async (requestId) => {
  return axiosInstance("/payments", {
    method: "POST",
    data: { requestId },
  })
    .then((response) => {
      console.log("Checkout Response: ", response.data);
      return response.data;
    })
    .catch((err) => {
      console.log("Error in checkout: ", err);
    });
};

export { fetchPaymentSheetParams };
