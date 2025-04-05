import axiosInstance from "../axiosInstance";

export default updateUserDetails = async (payload) => {
    try{
        const response = await axiosInstance('/users', {
            method: 'PUT',
            data: payload,
         });
        return response;
    }catch(e){
        return e;
    }
}
