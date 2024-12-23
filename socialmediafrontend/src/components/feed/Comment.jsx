import React, { useEffect, useState } from 'react'
import { getUserById } from '../../api/appUserApi';
import { addComment, getComments } from '../../api/commentApi';

const Comment = ({postId}) => {

  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');

  useEffect(()=> {
    const fetchPostComments = async() => {
      try{
        const response = await getComments(postId);
        const commentsWithUsername = await Promise.all(
          response.data.map(async (comment) => {
            const userResponse = await getUserById(comment.userId);
            return {...comment, username: userResponse.data.username};
          })
        )
        setComments(commentsWithUsername);
      } catch (err) {
        console.log(err);
      }
    }

    fetchPostComments();
  }, [postId])

  const handlePostComment = async() => {
    if (!newComment.trim()) {
      alert('Comment cannot be empty')
      return;
    } 

    try {
      const response = await addComment(postId, newComment);
      const userResponse = await getUserById(response.data.userId);
      const newCommentWithUsername = {
        ...response.data,
        username: userResponse.data.username
      }
      setComments((prevComments) => [...prevComments, newCommentWithUsername]);
      setNewComment("");
    } catch (err) {
      console.log(err)
      alert("Failed to add comment")
    }
  }

  return (
    <div>
      <p className="text-gray-800 font-semibold">Comments</p>
      <hr className="mt-2 mb-2" />
      {comments.map((comment, index) => (
        <div key={index} className="flex items-center space-x-2 mt-2">
          <div>
            <p className="text-gray-800 font-semibold">{comment.username}</p>
            <p className="text-gray-500 text-sm">{comment.content}</p>
          </div>
        </div>
      ))}
      
      <div className="mt-4 flex items-center space-x-2">
        <input
          type="text"
          placeholder="Write a comment..."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          className="border border-gray-300 rounded-lg p-2 flex-grow"
        />
        <button
          onClick={handlePostComment}
          className="bg-primary-600 text-white px-4 py-2 rounded-lg hover:bg-primary-800"
        >
          Post
        </button>
      </div>
    </div>
  )
}

export default Comment