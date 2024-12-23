import axios from "axios";

const API_BASE = "http://localhost:8080";

export const registerUser = async (userData) => {
    return await axios.post(`${API_BASE}/auth/register`, userData);
}

export const loginUser = async (providedCredentials) => {
    return await axios.post(`${API_BASE}/auth/login`, providedCredentials);
}

export const searchUsers = async (username) => {
    return await axios.get(`${API_BASE}/users/search?username=${username}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    });
}

export const getCurrentUser = async () => {
    return await axios.get(`${API_BASE}/users/current`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}

export const getUserById = async (userId) => {
    return await axios.get(`${API_BASE}/users/${userId}`, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    });
}

export const updateUser = async (user) => {
    return await axios.put(`${API_BASE}/users/update`, user, {
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("token")}`,
            "Content-Type": "application/json"
        }
    })
}