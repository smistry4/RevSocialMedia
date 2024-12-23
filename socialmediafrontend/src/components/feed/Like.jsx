import React, { useEffect } from 'react'
import { useState } from 'react';
import { fetchLikesOnPost, likePost, unLikePost } from '../../api/likeApi';

const Like = ({postId}) => {

    //const userId = parseInt(sessionStorage.getItem('userId'), 10);
    const [likes, setLikes] = useState(0);
    const [hasLiked, setHasLiked] = useState(false);

    useEffect(() => {
        const fetchLikes = async() => {
            try {
                const response = await fetchLikesOnPost(postId);
                setLikes(response.data.count);
                setHasLiked(response.data.likedByCurrentUser);
            } catch (err) {
                console.log(err);
            }
        }

        fetchLikes();
    }, [postId]);

    const handleLike = async () => {
        try {
            if (hasLiked) {
                await unLikePost(postId);
                setLikes((prev) => prev - 1);
                setHasLiked(false);
            } else {
                await likePost(postId);
                setLikes((prev) => prev + 1);
                setHasLiked(true);
            }
        } catch(err) {
            console.log("like error: ", err)
        }
    };
  return (
    <button
      onClick={handleLike}
      className={`flex justify-center items-center gap-2 px-2 hover:bg-primary-800 hover:text-white rounded-full p-1 ${hasLiked ? "bg-primary-600 text-white":"bg-gray-200 text-gray-800"}`}
    >
      <svg
        className="w-5 h-5 fill-current"
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
      >
        <path d="M12 21.35l-1.45-1.32C6.11 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-4.11 6.86-8.55 11.54L12 21.35z" />
      </svg>
      {hasLiked ? "Liked": "Like"}
      <span>{likes} Likes</span>
    </button>
  )
}

export default Like