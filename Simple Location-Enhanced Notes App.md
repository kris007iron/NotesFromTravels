# Simple Location-Enhanced Notes App

A streamlined notes application that incorporates all the required functionalities in a simple yet effective way. This app allows users to create, edit, and view notes that are enhanced with location data and an optional photo, all while synchronizing with a backend server.

---

## Key Features and Technologies

- **User Authentication**
  - **Registration and Login with JWT:**  
    Users register and log in securely. The app communicates with a backend server via Retrofit, using JWT tokens for session security.

- **Modern UI and Architecture**
  - **Material UI Components:**  
    Clean and modern UI built with Material UI components.
  - **ViewBinding/DataBinding:**  
    Efficient and type-safe UI management.
  - **MVVM Pattern:**  
    Clear separation of concerns between UI (View), business logic (ViewModel), and data (Model).

- **Fragment-Based Navigation**
  - The app is divided into fragments:
    - Authentication fragment (login/register).
    - Notes list fragment.
    - Note detail/edit fragment.
    - Map fragment for location display.

- **Local Data Persistence**
  - **Local Database (e.g., Room):**  
    Each note is stored locally to allow offline access and later synchronization with the backend.

- **Device Functionalities (Optional)**
  - **GPS and Maps Integration:**  
    Automatically capture the user's current location when creating/editing a note.  
    Display a map with markers for note locations.
  - **Camera Integration:**  
    Allow users to capture a photo to attach to a note.

- **Data Synchronization**
  - **Retrofit:**  
    Synchronize notes between the local database and the backend server, using JWT-secured API requests.

---

## App Flow

1. **Authentication Flow**
   - **Login/Register Fragment:**  
     Users create an account or log in. On successful authentication, a JWT token is received for secure API communication.

2. **Main Notes Interface**
   - **Notes List Fragment:**  
     Displays a list of saved notes from the local database, with a preview of each note.
   - **Note Detail/Edit Fragment:**  
     Users can view full note details, edit content, update the location or image, or create a new note.  
     - Integrates GPS to automatically capture the current location.
     - Integrates the camera to take a photo for the note.
   - **Map Fragment:**  
     Provides a visual map view with markers indicating where each note was created. Tapping a marker can show a preview of the note.

3. **Data Synchronization**
   - The app uses Retrofit to periodically or on-demand sync the local notes with the backend server, ensuring data consistency and backup, all secured with JWT.

---