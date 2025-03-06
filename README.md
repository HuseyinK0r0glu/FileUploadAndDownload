# File Upload and Download App

This is a full-stack application for uploading and downloading files using **Spring Boot (backend)** and **React (frontend)**.

## 📌 Features
- Upload files to the server
- View uploaded files in the frontend
- Download files by clicking on them
- Handle errors for missing or unsupported files
- CORS support for frontend-backend communication

## 🛠️ Technologies Used
- **Backend:** Java 17, Spring Boot, Spring MVC, REST API
- **Frontend:** React, Fetch, bootstrap
- **Other:** Apache Commons IO (file handling), CORS configuration

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/HuseyinK0r0glu/FileUploadAndDownload.git
cd FileUploadAndDownload
```

---

## 🔥 Backend Setup (Spring Boot)

### 1️⃣ Navigate to the Backend Directory
```bash
cd backend
```

### 2️⃣ Build & Run the Backend
Make sure you have **Java 17** and **Maven** installed.

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📄 API Endpoints

### 🔼 Upload a File
```http
POST /upload
```

#### Request:
- **Content-Type:** `multipart/form-data`
- **Body:** Select a file to upload

#### Example using `cURL`:
```bash
curl -X POST -F "file=@example.txt" http://localhost:8080/upload
```

#### Response:
```json
{
  "message": "File uploaded successfully: example.txt"
}
```

---

### 🔽 Download a File
```http
GET /download/{filename}
```

#### Example Request:
```bash
curl -O http://localhost:8080/download/example.txt
```

#### Response:
- Returns the file as an attachment.

---

## 🎨 Frontend Setup (React)

### 1️⃣ Navigate to the Frontend Directory
```bash
cd frontend
```

### 2️⃣ Install Dependencies
Make sure **Node.js** and **npm** are installed.

```bash
npm install
```

### 3️⃣ Start the React App
```bash
npm start
```
The frontend will be available at **`http://localhost:3000`**.

---

## 🔄 Backend & Frontend Connection
The React frontend communicates with the Spring Boot backend running at **`http://localhost:8080`**.  
The backend allows requests from **`http://localhost:3000`** (configured in `CorsConfig.java`).

---

## ⚙️ Configuration
You can change the file storage directory in `application.properties`:
```properties
file.upload-dir=uploads
```

---

## 🐛 Error Handling
- `404 Not Found` → If the file does not exist.
- `400 Bad Request` → If an invalid file is provided.

---

## 🐝 License
This project is open-source under the MIT License.

---

## 📧 Contact
If you have any questions, feel free to reach out to [Hüseyin Köroğlu](https://github.com/HuseyinK0r0glu).

