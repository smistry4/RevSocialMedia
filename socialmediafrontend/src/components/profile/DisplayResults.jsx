import React from 'react'

const DisplayResults = ({users, followStatus, handleFollowToggle}) => {
  return (
    <>
        {users.length > 0 && (
          <div class="absolute flex w-auto flex-col rounded-lg border border-slate-200 bg-white shadow-sm">
            <nav class="flex min-w-[240px] flex-col gap-1 p-1.5">
              {users.map((user) => (
                <div
                  key={user.userId}
                  role="button"
                  class="text-slate-800 flex items-center rounded-md p-3 transition-all hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100"
                >
                  <div>
                    <h6 class="text-slate-800 font-medium">
                      {user.firstName} {user.lastName}
                    </h6>
                    <p class="text-slate-500 text-sm">{user.username}</p>
                    <button
                      onClick={() => handleFollowToggle(user.userId)}
                      className={`${
                        followStatus[user.userId]
                          ? "bg-red-600 hover:bg-red-800"
                          : "bg-primary-600 hover:bg-blue-800"
                      } text-white text-sm font-medium py-1 px-2 rounded-md`}
                    >
                      {followStatus[user.userId] ? "Unfollow" : "Follow"}
                    </button>
                  </div>
                </div>
              ))}
            </nav>
          </div>
        )}
    </>
  )
}

export default DisplayResults