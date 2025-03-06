# Notes Endpoints Specification for Location-Enhanced Notes App

This document outlines the Notes endpoints that provide the necessary functionality for managing notes in the app. It serves as a guide for both backend and frontend developers to implement and integrate these endpoints securely and effectively.

---

## Overview

The Notes endpoints allow authenticated users to perform CRUD (Create, Read, Update, Delete) operations on their notes. Each note includes information such as title, content, location (latitude and longitude), and an optional image. All requests to these endpoints require a valid JWT included in the `Authorization` header.

---

## Endpoints

### 1. GET `/notes`

#### Description
Retrieves a list of all notes for the authenticated user.

#### Request

- **Method:** GET  
- **Endpoint:** `/notes`
- **Headers:**
  - `Authorization: Bearer <JWT token>`
  - `Accept: application/json`

#### Response

- **Success (200 OK):**
  - **Body (JSON):** An array of note objects.
  
**Example Response:**
```json
[
  {
    "id": "note123",
    "title": "Meeting Notes",
    "content": "Discuss project requirements...",
    "location": {
      "latitude": 40.7128,
      "longitude": -74.0060
    },
    "imageUrl": "https://example.com/images/note123.jpg",
    //"createdAt": "2025-02-01T10:00:00Z",
    //"updatedAt": "2025-02-02T12:00:00Z"
  },
  {
    "id": "note124",
    "title": "Grocery List",
    "content": "Milk, Bread, Eggs...",
    "location": null,
    "imageUrl": null,
    //"createdAt": "2025-02-03T08:30:00Z",
    //"updatedAt": "2025-02-03T08:30:00Z"
  }
]
```

- **Error Responses:**
  - **401 Unauthorized:** If the JWT is missing or invalid.
  - **500 Internal Server Error:** In case of unexpected server errors.

---

### 2. GET `/notes/{id}`

#### Description
Retrieves detailed information for a specific note identified by its ID.

#### Request

- **Method:** GET  
- **Endpoint:** `/notes/{id}`
- **Headers:**
  - `Authorization: Bearer <JWT token>`
  - `Accept: application/json`

#### Response

- **Success (200 OK):**
  - **Body (JSON):** A single note object.
  
**Example Response:**
```json
{
  "id": "note123",
  "title": "Meeting Notes",
  "content": "Discuss project requirements and milestones...",
  "location": {
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "imageUrl": "https://example.com/images/note123.jpg",
  //"createdAt": "2025-02-01T10:00:00Z",
  //"updatedAt": "2025-02-02T12:00:00Z"
}
```

- **Error Responses:**
  - **401 Unauthorized:** If the JWT is missing or invalid.
  - **404 Not Found:** If the note with the given ID does not exist.
  - **500 Internal Server Error:** In case of unexpected server errors.

---

### 3. POST `/notes`

#### Description
Creates a new note for the authenticated user. The note can include a title, content, location data, and an optional image.

#### Request

- **Method:** POST  
- **Endpoint:** `/notes`
- **Headers:**
  - `Authorization: Bearer <JWT token>`
  - `Content-Type: application/json`
- **Body Parameters (JSON):**
  - `title` (string, required): The title of the note.
  - `content` (string, required): The body text of the note.
  - `location` (object, optional):  
    - `latitude` (number, required if location provided)
    - `longitude` (number, required if location provided)
  - `imageUrl` (string, optional): URL of the attached image.

**Example Request Body:**
```json
{
  "title": "New Note",
  "content": "Details of the note...",
  "location": {
    "latitude": 37.7749,
    "longitude": -122.4194
  },
  "imageUrl": "https://example.com/images/newnote.jpg"
}
```

#### Response

- **Success (201 Created):**
  - **Body (JSON):** The created note object including its unique ID and timestamps.
  
**Example:**
```json
{
  "id": "note125",
  "title": "New Note",
  "content": "Details of the note...",
  "location": {
    "latitude": 37.7749,
    "longitude": -122.4194
  },
  "imageUrl": "https://example.com/images/newnote.jpg",
  //"createdAt": "2025-02-05T14:20:00Z",
  //"updatedAt": "2025-02-05T14:20:00Z"
}
```

- **Error Responses:**
  - **400 Bad Request:** If required fields are missing or invalid.
  - **401 Unauthorized:** If the JWT is missing or invalid.
  - **500 Internal Server Error:** In case of unexpected server errors.

---

### 4. PUT `/notes/{id}`

#### Description
Updates an existing note identified by its ID. Users can modify the title, content, location, and image.

#### Request

- **Method:** PUT  
- **Endpoint:** `/notes/{id}`
- **Headers:**
  - `Authorization: Bearer <JWT token>`
  - `Content-Type: application/json`
- **Body Parameters (JSON):**
  - `title` (string, optional): The updated title of the note.
  - `content` (string, optional): The updated content of the note.
  - `location` (object, optional):
    - `latitude` (number)
    - `longitude` (number)
  - `imageUrl` (string, optional): Updated URL for the attached image.

**Example Request Body:**
```json
{
  "title": "Updated Note Title",
  "content": "Updated details of the note...",
  "location": {
    "latitude": 37.7750,
    "longitude": -122.4183
  },
  "imageUrl": "https://example.com/images/updatednote.jpg"
}
```

#### Response

- **Success (200 OK):**
  - **Body (JSON):** The updated note object.
  
**Example:**
```json
{
  "id": "note123",
  "title": "Updated Note Title",
  "content": "Updated details of the note...",
  "location": {
    "latitude": 37.7750,
    "longitude": -122.4183
  },
  "imageUrl": "https://example.com/images/updatednote.jpg",
  //"createdAt": "2025-02-01T10:00:00Z",
  //"updatedAt": "2025-02-05T15:00:00Z"
}
```

- **Error Responses:**
  - **400 Bad Request:** If the provided data is invalid.
  - **401 Unauthorized:** If the JWT is missing or invalid.
  - **404 Not Found:** If the note with the given ID does not exist.
  - **500 Internal Server Error:** In case of unexpected server errors.

---

### 5. DELETE `/notes/{id}`

#### Description
Deletes a specific note identified by its ID from the authenticated user’s collection.

#### Request

- **Method:** DELETE  
- **Endpoint:** `/notes/{id}`
- **Headers:**
  - `Authorization: Bearer <JWT token>`
  - `Accept: application/json`

#### Response

- **Success (200 OK):**
  - **Body (JSON):** A message confirming the deletion.
  
**Example:**
```json
{
  "message": "Note deleted successfully."
}
```

- **Error Responses:**
  - **401 Unauthorized:** If the JWT is missing or invalid.
  - **404 Not Found:** If the note with the given ID does not exist.
  - **500 Internal Server Error:** In case of unexpected server errors.

---

## Key Implementation Considerations

### Backend Considerations
- **Authentication:**  
  All endpoints must validate the JWT token provided in the `Authorization` header before processing requests.
- **Data Validation:**  
  Validate all incoming data (e.g., ensure required fields are present and in the correct format).
- **Database Operations:**  
  Ensure that CRUD operations are performed securely and efficiently. Handle potential race conditions or conflicts.
- **Error Handling:**  
  Standardize error responses with clear messages and proper HTTP status codes.

### Frontend Considerations
- **Token Management:**  
  Ensure the JWT is included in the `Authorization` header for every request to these endpoints.
- **User Feedback:**  
  Provide meaningful error messages or feedback based on the standardized error responses.
- **Data Synchronization:**  
  Manage local state and data caching to improve responsiveness, while ensuring synchronization with the server.

### Security Best Practices
- **HTTPS:**  
  All communication between the client and server should occur over HTTPS.
- **Authorization:**  
  Only authenticated users should access these endpoints. The server must enforce proper authorization checks.

---