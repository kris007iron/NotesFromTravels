# Server Endpoints for Location-Enhanced Notes App

Below is a list of the necessary endpoints for the server-side implementation along with short descriptions for each:

---

## Authentication Endpoints

- **POST `/auth/register`**  
  Registers a new user by accepting user details (e.g., username, email, password) and creating a new account.

- **POST `/auth/login`**  
  Authenticates a user with provided credentials and returns a JWT token for securing subsequent API requests.

---

## Notes Endpoints

- **GET `/notes`**  
  Retrieves a list of all notes for the authenticated user. This endpoint supports filtering, pagination, or sorting as needed.

- **GET `/notes/{id}`**  
  Retrieves detailed information for a specific note identified by its ID.

- **POST `/notes`**  
  Creates a new note. The request payload includes note details such as title, content, location coordinates, and optionally, an image.

- **PUT `/notes/{id}`**  
  Updates an existing note identified by its ID. Allows modifications to the note content, location, and attached image.

- **DELETE `/notes/{id}`**  
  Deletes a specific note identified by its ID.

---

## Optional / Additional Endpoints

- **POST `/notes/{id}/image`**  
  (Optional) Handles image uploads for a specific note. This endpoint can process image files and associate them with the note.

- **GET `/notes/map`**  
  (Optional) Retrieves all notes with their location data. This endpoint can be used to display markers on a map view in the client application.

---

These endpoints provide a comprehensive API for managing user authentication and CRUD operations for notes, while also accommodating optional functionalities such as image uploads and map-based data retrieval.
