import React, { useEffect, useState } from "react";
import { getUserById } from "../../api/appUserApi";
import Comment from "./Comment"
import Like from "./Like"

const Post = ({post}) => {

  const [username, setUsername] = useState("");

  useEffect(() => {
    const fetchUserName = async() => {
      try{
        const response = await getUserById(post.userId);
        const user = response.data;
        setUsername(`${user.firstName} ${user.lastName}`);
      } catch (err) {
        console.log(err);
      }
    }

    fetchUserName();
  }, [post.userId])

  return (
    <div className="bg-white p-8 rounded-lg shadow-md max-w-md w-full">
    {/* Post Header */}
    <div className="flex items-center justify-between mb-4">
      <div className="flex items-center space-x-2">
        <div>
          <p className="text-gray-800 font-semibold">{username}</p>
        </div>
      </div>
    </div>

    {/* Post Content */}
    <div className="mb-4">
      <p className="text-gray-800">
        {post.content}{" "}
      </p>
    </div>

    {/* Likes and Comments */}
    <div className="flex items-center justify-between text-gray-500">
      <Like postId={post.postId} />
      <p className="text-gray-500">Comment(s)</p>
      {/* {post.comments.length} */}
    </div>

    <hr className="mt-2 mb-2" />

    {/* Comments Section */}
    <Comment postId={post.postId} />
  </div>
  );
};

export default Post;
