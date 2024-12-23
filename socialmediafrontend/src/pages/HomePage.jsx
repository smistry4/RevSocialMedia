import React from 'react'
import { fetchFeed } from '../api/postApi';
import Post from '../components/feed/Post'
import { useState, useEffect } from 'react';
import CreatePost from '../components/feed/CreatePost';
import SearchUser from '../components/profile/SearchUser';
import ProfileCard from '../components/profile/ProfileCard';
import { getCurrentUser } from '../api/appUserApi';

const HomePage = () => {

  const [posts, setPosts] = useState([]);
  const [user, setUser] = useState();

  useEffect(() =>{
    const loadFeed = async() => {
      try {
        const response = await fetchFeed();
        setPosts(response.data);
      } catch(err) {
        console.log(err);
      }
    }
    loadFeed();
  },[])

  useEffect(() => {
    const loadUser = async () => {
      const response = await getCurrentUser();
      //console.log(response.data)
      setUser(response.data)
    }

    loadUser();
  },[])

  const addNewPostToFeed = (newPost) => {
    setPosts((prevPosts) => [newPost, ...prevPosts]);
  }

  return (
    <div className="h-screen grid grid-cols-3">
      <div className="bg-gray-200 p-4 flex flex-col items-center">
        {user ? (
          <ProfileCard user={user} />
        ) : (
          <p>Loading profile...</p>
        )}
      </div>
      <div className="bg-gray-100 flex flex-col items-center p-4 space-y-4 overflow-y-auto">
        <CreatePost addNewPostToFeed={addNewPostToFeed} />
        {posts.map((post) => (
          <Post key={post.postId} post={post} />
        ))}
      </div>
      <div className="bg-gray-200 p-4 flex flex-col items-center">
        <SearchUser/>
      </div>
    </div>
  );
}

export default HomePage