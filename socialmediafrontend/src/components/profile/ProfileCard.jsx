import React from 'react'
import { Link } from 'react-router-dom'

const ProfileCard = ({user}) => {

  return (
      <div className="w-full max-w-sm bg-white border border-gray-200 rounded-lg shadow dark:bg-gray-800 dark:border-gray-700">
      <div className="flex justify-end px-4 pt-4">
      </div>
      <div className="flex flex-col items-center pb-10">
          <h5 className="mb-1 text-xl font-medium text-gray-900 dark:text-white">{user.firstName} {user.lastName}</h5>
          <span className="text-sm text-gray-500 dark:text-gray-400">{user.username}</span>
          <div className="flex flex-row mt-4 md:mt-6 ">
            <Link className="inline-flex items-center mx-2 px-4 py-2 text-sm font-medium text-center text-white bg-primary-600 rounded-lg hover:bg-primary-800" to="/profile">Profile</Link>
            <Link onClick={() => {localStorage.clear();}} className="inline-flex items-center mx-2 px-4 py-2 text-sm font-medium text-center text-white bg-red-600 rounded-lg hover:bg-red-800" to="/">Logout</Link>
          </div>
      </div>
    </div>
  )
}

export default ProfileCard