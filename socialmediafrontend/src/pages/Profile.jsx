import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getCurrentUser, updateUser } from "../api/appUserApi";
import { getFollowers, getFollowing } from "../api/relationshipApi";
import RelationshipList from "../components/profile/RelationshipList";

const Profile = () => {
  const [user, setUser] = useState(null);
  const [formData, setFormData] = useState({
    userId: "",
    firstName: "",
    lastName: "",
    username: "",
    email: "",
    bio: "",
    password: "",
  });
  const [followers, setFollowers] = useState([]);
  const [following, setFollowing] = useState([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const loadUser = async () => {
      try {
        const response = await getCurrentUser();
        setUser(response.data);
        setFormData({
          userId: response.data.userId,
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          username: response.data.username,
          email: response.data.email,
          bio: response.data.bio || "",
          password: "",
        });
      } catch (err) {
        console.error("Error loading user:", err);
      }
    };

    const getCountRelationships = async () => {
      try {
        const followersResponse = await getFollowers();
        const followingResponse = await getFollowing();
        setFollowers(followersResponse.data);
        setFollowing(followingResponse.data);
      } catch (err) {
        console.log("Error getting relationship count", err);
      }
    };

    loadUser();
    getCountRelationships();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await updateUser(formData);
      if (response.status >= 200 && response.status < 300) {
        const authorizationHeader = response.headers["authorization"];
        const valueToStore = authorizationHeader.split(" ")[1];
        localStorage.setItem("token", valueToStore);
      }
      setMessage("Profile updated successfully!");
    } catch (err) {
      console.error("Error updating profile:", err);
      setMessage("Failed to update profile. Please try again.");
    }
  };

  if (!user) return <p>Loading profile...</p>;

  return (
    <div className="min-h-screen bg-gray-100 flex flex-row items-start justify-between p-4 space-x-4">
      <div className="w-1/4 bg-white border border-gray-200 rounded-lg shadow p-4">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Followers</h3>
        <RelationshipList list={followers} />
      </div>
      <div className="w-1/2 max-w-2xl bg-white border border-gray-200 rounded-lg shadow p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-semibold text-gray-800">
            Profile Settings
          </h2>
          <Link
            className="text-sm font-medium text-gray-600 bg-gray-200 px-4 py-2 rounded-lg hover:bg-gray-300"
            to="/feed"
          >
            Back
          </Link>
        </div>

        {/* Follower/Following Count */}
        <div className="flex justify-between mb-6">
          <div className="text-center">
            <h3 className="text-xl font-bold text-gray-900">{followers.length}</h3>
            <p className="text-gray-600">Followers</p>
          </div>
          <div className="text-center">
            <h3 className="text-xl font-bold text-gray-900">{following.length}</h3>
            <p className="text-gray-600">Following</p>
          </div>
        </div>

        {/* Profile Update Form */}
        <form onSubmit={handleFormSubmit} className="space-y-4">
          {/* First Name */}
          <div>
            <label
              htmlFor="firstName"
              className="block text-sm font-medium text-gray-700"
            >
              First Name
            </label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={formData.firstName}
              onChange={handleInputChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
              required
            />
          </div>

          {/* Last Name */}
          <div>
            <label
              htmlFor="lastName"
              className="block text-sm font-medium text-gray-700"
            >
              Last Name
            </label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              value={formData.lastName}
              onChange={handleInputChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
              required
            />
          </div>

          {/* Username */}
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium text-gray-700"
            >
              Username
            </label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
              required
            />
          </div>

          {/* email */}
          <div>
            <label
              htmlFor="email"
              className="block text-sm font-medium text-gray-700"
            >
              Email
            </label>
            <input
              type="text"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
              required
            />
          </div>

          {/* Bio */}
          <div>
            <label
              htmlFor="bio"
              className="block text-sm font-medium text-gray-700"
            >
              Bio
            </label>
            <textarea
              id="bio"
              name="bio"
              value={formData.bio}
              onChange={handleInputChange}
              rows="3"
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
            ></textarea>
          </div>

          {/* Password */}
          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium text-gray-700"
            >
              Password
            </label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-primary-500 focus:border-primary-500"
              placeholder="Leave blank to keep the current password"
            />
          </div>

          {/* Save Button */}
          <button
            type="submit"
            className="w-full bg-primary-600 text-white py-2 px-4 rounded-lg hover:bg-primary-800 focus:ring-4 focus:ring-primary-300"
          >
            Save Changes
          </button>
        </form>

        {/* Message */}
        {message && (
          <p className="mt-4 text-center text-green-500">{message}</p>
        )}
      </div>
      <div className="w-1/4 bg-white border border-gray-200 rounded-lg shadow p-4">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Following</h3>
        <RelationshipList list={following} />
      </div>
    </div>
  );
};

export default Profile;
