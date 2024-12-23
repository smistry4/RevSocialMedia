import React, { useEffect, useState } from 'react'
import { getUserById } from '../../api/appUserApi';
import { followUser, unFollowUser, isFollowing } from '../../api/relationshipApi';
import DisplayResults from './DisplayResults';

const RelationshipList = ({list}) => {
  const [users, setUsers] = useState([]);
  const [followStatus, setFollowStatus] = useState({});

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

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const fetchedUsers = [];
        for (const userId of list) {
          const userResponse = await getUserById(userId);
          fetchedUsers.push(userResponse.data);
        }
        setUsers(fetchedUsers);
      } catch (err) {
        console.error("Error fetching users", err);
      }
    };

    fetchUsers();
  }, [list]);

  useEffect(() => {
    const fetchFollowStatus = async () => {
      try {
        const statuses = {};
        await Promise.all(
          list.map(async (user) => {
            const statusResponse = await isFollowing(user);
            statuses[user] = statusResponse.data.following;
          })
        );
        setFollowStatus(statuses);
      } catch (err) {
        console.error("Error fetching follow statuses", err);
      }
    };

    if (list && list.length > 0) {
      fetchFollowStatus();
    }
  }, [list]); 

  return (
    <>
      <DisplayResults users={users} followStatus={followStatus} handleFollowToggle={handleFollowToggle}/>
    </>
  )
}

export default RelationshipList