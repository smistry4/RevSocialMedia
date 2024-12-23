import axios from "axios";

const API_BASE = "http://localhost:8080";

export const createPost = async (postData) => {
    return await axios.post(`${API_BASE}/posts`, postData, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const fetchFeed = async () => {
    return await axios.get(`${API_BASE}/posts/feed`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        }
    })
}