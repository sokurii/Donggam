import axiosInstance from "./axiosConfig";


// 위치 기반 데이터 
export const locationInfo = async (memberId, latitude, longitude) => {
  try {
    if (memberId && latitude !== null && longitude !== null) {
      const res = await axiosInstance.post(`/main`, {
        memberId: memberId,
        latitude: latitude,
        longitude: longitude,
      });

      if (res.status === 200) {
        return res.data;
      }
      else {
        // console.error('위치 정보 API 요청 실패');
        return null;
      }
    }
  } catch (error) {
    return error;
  }
};