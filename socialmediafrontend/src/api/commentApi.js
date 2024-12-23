import axios from "axios";

const API_BASE = "http://localhost:8080";

export const addComment = async (postId, comment) => {
    return await axios.post(`${API_BASE}/posts/${postId}/comments`, comment, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const getComments = async (postId) => {
    return await axios.get(`${API_BASE}/posts/${postId}/comments`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}