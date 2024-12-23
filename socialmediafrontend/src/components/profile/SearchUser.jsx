import React, { useState, useRef, useEffect } from "react";
import { searchUsers } from "../../api/appUserApi";
import {
  followUser,
  unFollowUser,
  isFollowing,
} from "../../api/relationshipApi";
import DisplayResults from "./DisplayResults";

const SearchUser = () => {
  const [userInput, setUserInput] = useState("");
  const [users, setUsers] = useState([]);
  const [followStatus, setFollowStatus] = useState({});
  const searchContainerRef = useRef(null);

  const handleFollowToggle = async (userId) => {
    try {
      if (followStatus[userId]) {
        await unFollowUser(userId);
        setFollowStatus((prev) => ({ ...prev, [userId]: false }));
      } else {
        await followUser(userId);
        setFollowStatus((prev) => ({ ...prev, [userId]: true }));
      }
    } catch (err) {
      console.log(err);
      alert("Unable to update follow status");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!userInput.trim()) {
      return;
    }

    try {
      const response = await searchUsers(userInput);
      const users = response.data;
      setUsers(response.data);
      const statuses = {};
      await Promise.all(
        users.map(async (user) => {
          
          const statusResponse = await isFollowing(user.userId);
          statuses[user.userId] = statusResponse.data.following;
        })
      );
      setFollowStatus(statuses);
    } catch (err) {
      console.log(err);
    }
  };

  const clearSearch = () => {
    setUsers([]);
    setUserInput("");
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        searchContainerRef.current &&
        !searchContainerRef.current.contains(event.target)
      ) {
        clearSearch();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  return (
    <>
      <div className="w-full mx-auto" ref={searchContainerRef}>
        <form className="relative" onSubmit={handleSubmit}>
          <label
            htmlFor="default-search"
            className="mb-2 text-sm font-medium text-gray-900 sr-only dark:text-white"
          >
            Search
          </label>
          <div className="relative">
            <div className="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
              <svg
                className="w-4 h-4 text-gray-500 dark:text-gray-400"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 20 20"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
                />
              </svg>
            </div>
            <input
              type="search"
              id="default-search"
              value={userInput}
              onChange={(e) => setUserInput(e.target.value)}
              className="block w-full p-6 ps-10 text-sm text-gray-900 border border-gray-300 rounded-lg bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
              placeholder="Find People.."
              required
            />
            <button
              type="submit"
              className="text-white absolute my-auto end-3.5 bottom-4 bg-primary-600 hover:bg-primary-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-4 py-2 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
            >
              Search
            </button>
          </div>
        </form>
        <DisplayResults users={users} followStatus={followStatus} handleFollowToggle={handleFollowToggle}/>
      </div>
    </>
  );
};

export default SearchUser;
