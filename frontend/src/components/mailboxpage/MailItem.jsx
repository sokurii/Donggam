import React, { useEffect, useState } from 'react';
import { getMailDetail, postMailLike } from '../../api/mailApi';
import like from '../../assets/like/full_heart.png'
import dislike from '../../assets/like/empty_heart.png'


const MailItem = ({ isOpen, onClose, mailData }) => {
  const handleBackgroundClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  if (!isOpen) return null;

  const mailId = mailData.messageId;

  const [mailDetail, setMailDetail] = useState({});
  const [isLiked, setIsLiked] = useState(false);

  useEffect(() => {
    getMailDetail(mailId)
      .then((res) => {
        setMailDetail(res);
        console.log('쪽지디테일내놧!', res)
      })
      .catch((err) => {
        console.log('쪽지 detail 가져오기 실패:', err);
      });
  }, []);

  const handleLikeClick = () => {
    postMailLike(mailId)
    setIsLiked(!isLiked);
  };

  const handleReportClick = () => {
    // 신고하기 버튼을 클릭했을 때 로직을 추가하세요.
  };

  return (
    <>
      <div
        className={`${isOpen ? 'show' : ''} 
        top-0 left-0 w-full h-full fixed flex justify-center align-center`}
        style={{
          backgroundColor: 'rgba(0, 0, 0, 0.2)',
          padding: '0 28px 0',
          zIndex: 3,
        }}
        onClick={handleBackgroundClick}
      >
        <div
          style={{
            backgroundColor: 'white',
            borderRadius: '20px',
            width: '100%',
            height: '55%',
            margin: 'auto',
          }}
        >
          <div>
            <p className="text-center text-sm text-gray-500 mt-2">쪽지 작성 일자</p>
          </div>
          <div>
            {/* 쪽지 내용 */}
            <h1 className="text-center mt-4">{mailDetail.content}</h1>
          </div>
          <div>
            {/* 보낸 사람 이름 */}
            <p className="text-center mt-4">{mailDetail.from}</p>
          </div>
          <div>
            {/* 신고하기 버튼 */}
            <button onClick={handleReportClick} className="text-center mt-4 bg-red-500 text-white py-2 px-4 rounded-full">신고하기</button>
          </div>
          <div>
            {/* 좋아요 버튼 */}
            {isLiked ? (
              <img src={like} onClick={handleLikeClick} />
            ) : (
              <img src={dislike} onClick={handleLikeClick} />
            )}
          </div>
        </div>
      </div >
    </>
  );
};

export default MailItem;