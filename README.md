# UniShare — P2P Rental Platform

A Spring Boot backend for a peer-to-peer student rental marketplace. Students can list items for rent, browse and book listings, message each other, arrange campus meetups, pay for bookings, and leave reviews.

## Features

- **Authentication & Profiles** — JWT-based registration/login, password reset via email OTP, profile management
- **Listings** — create, update, delete, and browse rental listings with filtering (keyword, category, condition, status, price range) and pagination
- **Listing Images** — multipart image upload per listing
- **Bookings** — full booking lifecycle: create → confirm → complete/cancel, with overlap-safe scheduling
- **Meetup Locations** — attach a campus meetup location to a booking
- **Payments** — process and retrieve payments tied to a booking, supporting dual (cash/online) workflows
- **Reviews** — leave reviews restricted to renters/owners of completed bookings; public browsing of listing and user reviews
- **Chat** — direct messaging between users scoped to a specific listing, with conversation history
- **Admin Panel** — platform stats, user management (roles, activation), listing moderation, booking oversight — role-protected (`ADMIN`)

## Tech Stack

- **Language/Framework:** Java, Spring Boot
- **Security:** Spring Security, JWT (JJWT)
- **Persistence:** Spring Data JPA / Hibernate, MySQL
- **Migrations:** Flyway
- **Mapping:** MapStruct (DTO mapping)
- **Docs:** springdoc-openapi (Swagger UI)
- **Email:** Spring Mail (password reset OTP)
- **Validation:** Jakarta Bean Validation
- **Build:** Maven

## API Endpoints

### Authentication — `/api/auth`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Authenticate and receive a JWT |
| POST | `/api/auth/forgot-password` | Request a password reset OTP via email |
| POST | `/api/auth/reset-password` | Reset password using OTP |

### Users — `/api/users`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/users/me` | Get the authenticated user's profile |
| PUT | `/api/users/me` | Update the authenticated user's profile |
| PUT | `/api/users/me/password` | Change the authenticated user's password |
| GET | `/api/users/{id}` | Get a user's public profile |

### Listings — `/api/listing`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/listing/create` | Create a new listing |
| GET | `/api/listing` | Browse listings (filter by keyword, category, condition, status, price range; paginated) |
| GET | `/api/listing/{id}` | Get a listing by ID |
| PUT | `/api/listing/{id}` | Update a listing (owner only) |
| DELETE | `/api/listing/{id}` | Delete a listing (owner only) |
| GET | `/api/listing/user/{id}` | Get all listings created by a user |

### Listing Images — `/api/listings/{listingId}/images`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/listings/{listingId}/images` | Upload one or more images for a listing (multipart/form-data) |
| GET | `/api/listings/{listingId}/images` | Get all images for a listing |

### Bookings — `/api/bookings`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/bookings/create` | Create a booking |
| GET | `/api/bookings/my` | Get bookings made by the authenticated user |
| GET | `/api/bookings/incoming` | Get bookings received as a listing owner |
| GET | `/api/bookings/{id}` | Get a booking by ID |
| PUT | `/api/bookings/{id}/cancel` | Cancel a booking |
| PUT | `/api/bookings/{id}/confirm` | Confirm a booking |
| PUT | `/api/bookings/{id}/complete` | Mark a booking as completed |
| PUT | `/api/bookings/{id}/meetup` | Attach a meetup location to a booking |

### Meetup Locations — `/api/meetup-locations`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/meetup-locations` | Get all active campus meetup locations |

### Payments — `/api/payments`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/payments/{bookingId}` | Process a payment for a booking |
| GET | `/api/payments/{bookingId}` | Get payment details for a booking |

### Reviews — `/api/reviews`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/reviews/create` | Submit a review (renter/owner of a completed booking) |
| GET | `/api/reviews/listing/{id}` | Get all reviews for a listing (public) |
| GET | `/api/reviews/user/{id}` | Get all reviews received by a user (public) |

### Chat — `/api/chat`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/chat/{listingId}/{receiverId}` | Send a message about a listing |
| GET | `/api/chat/{listingId}/{userId}` | Get the conversation with a user about a listing |
| GET | `/api/chat/conversations` | Get all conversations for the authenticated user |

### Admin — `/api/admin` *(requires `ADMIN` role)*

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/stats` | Get platform-wide statistics |
| GET | `/api/admin/users` | List all users (filterable by name, paginated) |
| GET | `/api/admin/users/{id}` | Get a user by ID |
| PUT | `/api/admin/users/{id}/role` | Change a user's role |
| PUT | `/api/admin/users/{id}/deactivate` | Deactivate a user |
| PUT | `/api/admin/users/{id}/activate` | Activate a user |
| GET | `/api/admin/listings` | List all listings (paginated) |
| PUT | `/api/admin/listings/{id}/deactivate` | Deactivate a listing |
| PUT | `/api/admin/listings/{id}/activate` | Activate a listing |
| GET | `/api/admin/bookings` | List all bookings (paginated) |

## Getting Started

### Prerequisites

- Java 17+ (or the JDK version matching `pom.xml`)
- Maven
- MySQL

### Configuration

Set up `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/unishare_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: "<your-password>"
  flyway:
    enabled: true

jwt:
  secret: "<your-jwt-secret>"
  expiration: 86400000

file:
  upload-dir: uploads
```

Password reset emails require SMTP credentials (e.g., Mailtrap for local testing):

```yaml
spring:
  mail:
    host: sandbox.smtp.mailtrap.io
    port: 2525
    username: <your-mailtrap-username>
    password: "<your-mailtrap-password>"
```

### Run the App

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on port `8080` by default. Flyway runs migrations automatically on startup; schema changes are managed exclusively through migration scripts (`ddl-auto: validate`).

### API Docs

Swagger UI is available (via springdoc-openapi) once the app is running — typically at `/swagger-ui.html` or `/swagger-ui/index.html`.

## Notes

- Authentication uses a JWT stored in the `Authorization` header (`Bearer <token>`); the username principal is the user's university email
- DTO mapping is handled with MapStruct throughout
- Listing images are stored under the configured `file.upload-dir`
