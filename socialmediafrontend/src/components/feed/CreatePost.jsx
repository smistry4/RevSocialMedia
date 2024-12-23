import React, { useState } from "react";
import { createPost } from "../../api/postApi";

const CreatePost = ({addNewPostToFeed}) => {

    const [content, setContent] = useState("");
    //const userId = parseInt(sessionStorage.getItem("userId"), 10);
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!content.trim()) {
            alert("Empty post");
            return;
        }

        try{
            const postData = {content};
            const response = await createPost(postData);
            addNewPostToFeed(response.data);
            setContent("");

        }catch(err) {
            console.log(err);
            alert("Cannot create Post");
        }
    }

  return (
    <div className="p-8 rounded-lg shadow-md max-w-md w-full bg-primary-400">
      <form onSubmit={handleSubmit}>
        <label
          for="message"
          class="block mb-2 text-large font-medium text-white"
        >
          Create Post
        </label>
        <textarea
          id="message"
          rows="4"
          value={content}
          onChange={(e)=>setContent(e.target.value)}
          class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          placeholder="What's on your mind?"
        ></textarea>
        <button
          type="submit"
          className="mt-4 bg-primary-600 text-white py-2 px-4 rounded-md hover:bg-primary-800"
        >
          Post
        </button>
      </form>
    </div>
  );
};

export default CreatePost;
