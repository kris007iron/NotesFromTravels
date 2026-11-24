# NotesFromTravels

A location-enhanced notes application developed as a final school project. The application lets users create, edit, and manage notes enriched with geographic locations and optional images.

---

## Key Features

- **Authentication:** GitHub OAuth2 login, JWT-based authentication, token renewal and invalidation.
- **Notes Management:** CRUD operations with user-specific access.
- **Maps Integration:** Location selection, place search, and visualization of saved locations using Google Maps.
- **Local Data Persistence:** Local storage using Room and communication with the backend through Retrofit.
- **User Administration:** Web-based panel for managing users and their roles.

---

## Technologies

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL.
- **Mobile:** Native Android (Java), Room, Retrofit, Google Maps API.
- **Web:** React, TypeScript, Vite, shadcn/ui.

---

## Documentation

- [Application Overview](Simple%20Location-Enhanced%20Notes%20App.md)
- [Server Endpoints](Endpoint%20Description.md)
- [Authentication Specification](Auth.md)
- [Notes Endpoints Specification](Notes.md)

---

## Project Status

The project represents the original school project implementation. Further improvements could include security hardening, automated testing, and configuration management.
