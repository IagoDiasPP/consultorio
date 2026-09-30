import axios from "axios"

const api = axios.create({
  baseURL: "https://consultorio-backend-00q5.onrender.com"
})

export default api