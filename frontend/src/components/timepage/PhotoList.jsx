import React, { useState, useEffect } from "react";
import axiosInstance from "../../api/axiosConfig";
import { Link } from "react-router-dom";
import Masonry from "react-masonry-component";
import fullLikeImg from "../../assets/like/full_heart.png";
import nullLogo from "../../assets/images/noPhoto.svg";
import CreateBtn from "../timepage/CreateBtn";

const PhotoList = ({ setTotalParticipants, totalParticipants, remainTime, isBestTime }) => {
  const [photos, setPhotos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getPhotos = async () => {
      try {
        const response = await axiosInstance.get("/time");
        if (response.data && response.data.data) {
          setPhotos(response.data.data);
          setTotalParticipants(response.data.data.length);
          // console.log('왔니?', response);
        }
        setLoading(false);
      } catch (error) {
        // console.error("에러", error);
        setLoading(false);
      }
    };

    getPhotos();
  }, []);

  const masonryOptions = {
    itemSelector: ".masonry-grid-item",
    transitionDuration: 0,
  };

  return (
    <div className="px-5 bg-white h-[73vh]">
      {loading ? (
        <h4 className="text-center">Loading...</h4>
      ) : photos.length === 0 ? (
        <img src={nullLogo} alt="No Photos" className="mx-auto py-8" />
      ) : (
        <Masonry className={"my-gallery-class overflow-auto max-h-[calc(100vh-300px)]"} options={masonryOptions}>
          {photos.map((photo) => (
            <div
              key={photo.imageId}
              className="masonry-grid-item "
              style={{ width: "50%" }}
            >
              <Link
                to={`/time/${photo.imageId}`}
                state={{isBestTime, totalParticipants, remainTime}}
                className="m-2 flex flex-col items-center relative"
              >
                <div className="overflow-hidden">
                  <img
                    src={photo.imageAddress}
                    alt={photo.title}
                    className="rounded-lg"
                  />
                  {photo.isLiked ? (
                    <img
                      src={fullLikeImg}
                      alt="Liked"
                      className="absolute top-0 left-0 m-3"
                    />
                  ) : null}
                </div>
                <h5 className="mt-1">{photo.title}</h5>
              </Link>
            </div>
          ))}   
        </Masonry>
      )}
      <CreateBtn to="/time/upload" />  
    </div>
  );
};

export default PhotoList;
