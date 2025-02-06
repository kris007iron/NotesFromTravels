# OAuth and JWT Authentication Endpoints for Location-Enhanced Notes App

This document provides a comprehensive specification for the authentication endpoints, integrating both OAuth2 and JWT mechanisms. It serves as a guide for both backend and frontend developers to implement and interact with the authentication system.

---

## Overview

The authentication system consists of two main components:
- **User Registration:** An endpoint to register new users.
- **OAuth2 Token Flow:** Endpoints for handling OAuth authorization and token issuance. The issued access token is a JWT used to secure subsequent API requests.

---

## 1. User Registration Endpoint

### POST `/auth/register`

#### Description
Registers a new user by accepting user details, creating an account, and returning confirmation data.

#### Request

- **Method:** POST  
- **Endpoint:** `/auth/register`
- **Headers:**  
  - `Content-Type: application/json`
- **Body Parameters (JSON):**
  - `username` (string, required): Desired username.
  - `email` (string, required): User’s email address.
  - `password` (string, required): User’s password.

**Example Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecureP@ssw0rd!"
}
```

#### Response

- **Success (201 Created):**
  - **Body (JSON):**
    - `id` (string or number): Unique identifier for the new user.
    - `username` (string)
    - `email` (string)
    - `message` (string): Confirmation message.

**Example:**
```json
{
  "id": "12345",
  "username": "john_doe",
  "email": "john@example.com",
  "message": "User registered successfully."
}
```

- **Error Responses:**
  - **400 Bad Request:** For missing or invalid fields.
  - **409 Conflict:** If the username or email already exists.

---

## 2. OAuth2 and JWT Token Endpoint

### POST `/oauth/token`

#### Description
Handles OAuth2 token requests. This endpoint accepts user credentials (or an authorization code, depending on the grant type) and returns an access token—implemented as a JWT—along with other token metadata.

#### Request

- **Method:** POST  
- **Endpoint:** `/oauth/token`
- **Headers:**
  - `Content-Type: application/x-www-form-urlencoded`
  - Optionally, include: `Authorization: Basic <base64(client_id:client_secret)>` if client credentials are required.
- **Body Parameters (Form URL Encoded):**
  - For **Resource Owner Password Credentials Grant:**
    - `grant_type` (string, required): Must be set to `password`.
    - `username` (string, required): The user's username or email.
    - `password` (string, required): The user's password.
  - For other grant types (e.g., Authorization Code Grant), additional parameters such as `code`, `redirect_uri`, etc., may be required.

**Example Request Body (Resource Owner Password Credentials):**
```
grant_type=password&username=john_doe&password=SecureP@ssw0rd!
```

#### Response

- **Success (200 OK):**
  - **Body (JSON):**
    - `access_token` (string): The JWT access token.
    - `token_type` (string): Typically `Bearer`.
    - `expires_in` (number): Lifetime of the token in seconds.
    - `refresh_token` (string, optional): Token to obtain a new access token, if implemented.
    - `scope` (string, optional): Scope of the access granted.

**Example:**
```json
{
  "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 3600,
  "refresh_token": "def50200f7a...",
  "scope": "read write"
}
```

- **Error Responses:**
  - **400 Bad Request:** For missing or invalid parameters.
  - **401 Unauthorized:** If the credentials are invalid.

---

## Key Implementation Considerations

### JWT Integration
- **Access Token as JWT:**  
  The access token provided by the `/oauth/token` endpoint is a JWT, signed using a secure algorithm (e.g., HS256 or RS256). It should include claims such as the user ID, roles, and token expiration.

### Backend Considerations
- **Data Validation:**  
  Validate all input data (e.g., email format, password strength).
- **Password Security:**  
  Hash passwords before storage.
- **Spring Security Configuration:**  
  Configure Spring Security with OAuth2 support, defining client details, token lifetimes, and scopes.
- **Error Handling:**  
  Provide standardized error responses to facilitate client-side error processing.

### Frontend Considerations
- **Input Validation:**  
  Validate registration and login inputs on the client side.
- **Token Storage:**  
  Securely store the JWT token using appropriate methods (e.g., secure in-memory or encrypted storage).
- **Authorization Header:**  
  Include the JWT in the `Authorization` header (`Bearer <token>`) for subsequent API calls.
- **Token Management:**  
  Implement token refresh logic to handle token expiration if a refresh token is provided.

### Security Best Practices
- **HTTPS:**  
  Ensure all communications occur over HTTPS to secure sensitive data such as passwords and tokens.
- **JWT Handling:**  
  Both client and server must handle JWTs securely to prevent unauthorized access.