import axios from "axios";

const API_BASE = "http://localhost:8080";

export const followUser = async (followedId) => {
    return await axios.post(`${API_BASE}/users/${followedId}/follow`, null, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const unFollowUser = async (followedId) => {
    return await axios.delete(`${API_BASE}/users/${followedId}/follow`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const getFollowers = async () => {
    return await axios.get(`${API_BASE}/users/followers`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const getFollowing = async () => {
    return await axios.get(`${API_BASE}/users/following`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const isFollowing = async(otherId) => {
    return await axios.get(`${API_BASE}/users/${otherId}/relation`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}
