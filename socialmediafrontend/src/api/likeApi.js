import axios from "axios";

const API_BASE = "http://localhost:8080";

export const likePost = async (postId) => {
    return await axios.post(`${API_BASE}/posts/${postId}/like`,null, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const fetchLikesOnPost = async (postId) => {
    return await axios.get(`${API_BASE}/posts/${postId}/like`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const unLikePost = async (postId) => {
    return await axios.delete(`${API_BASE}/posts/${postId}/like`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}