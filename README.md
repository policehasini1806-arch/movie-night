# 🎬 Movie Night Decision Maker

A full-stack web app to settle the age-old question: *What are we watching tonight?*

Create a room, invite friends, vote on genres, and get movie recommendations powered by [TMDB](https://www.themoviedb.org/).

---

## 🚀 Tech Stack

| Layer      | Technology                  |
|------------|-----------------------------|
| Backend    | Spring Boot 3 (Java 17)     |
| Database   | MySQL 8                     |
| Frontend   | HTML / CSS / Vanilla JS     |
| Movies API | TMDB (The Movie Database)   |

---

## 📁 Project Structure

```
movie-night-decision-maker/
├── backend/               Spring Boot app
│   └── src/main/java/com/movienight/
│       ├── controller/    REST endpoints
│       ├── service/       Business logic
│       ├── repository/    JPA repositories
│       ├── model/         JPA entities
│       ├── dto/           Request DTOs
│       ├── config/        CORS config
│       └── exception/     Error handling
├── frontend/              Static HTML/CSS/JS
│   ├── index.html
│   ├── create-room.html
│   ├── join-room.html
│   ├── vote.html
│   ├── movies.html
│   ├── result.html
│   ├── css/style.css
│   └── js/
├── database/schema.sql
├── pom.xml
└── README.md
```

---

## ⚙️ Setup & Run

### 1. MySQL Database

```sql
CREATE DATABASE movie_night_db;
```

Then run `database/schema.sql`.

### 2. Backend Config

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=yourpassword
tmdb.api.key=YOUR_TMDB_API_KEY
```

Get a free TMDB key at: https://www.themoviedb.org/settings/api

### 3. Run Backend

```bash
cd backend
mvn spring-boot:run
```

API runs on `http://localhost:8080`

### 4. Run Frontend

Open `frontend/index.html` with **Live Server** (VS Code extension) on port 5500,  
or use any static file server.

---

## 🌐 REST API Endpoints

| Method | Endpoint                              | Description             |
|--------|---------------------------------------|-------------------------|
| POST   | `/api/rooms/create`                   | Create a room           |
| GET    | `/api/rooms/{roomCode}`               | Get room details        |
| POST   | `/api/participants/join`              | Join a room             |
| GET    | `/api/participants/room/{roomCode}`   | List participants       |
| POST   | `/api/votes/submit`                   | Submit a genre vote     |
| GET    | `/api/votes/results/{roomCode}`       | Get vote results        |
| GET    | `/api/votes/check/{pid}/{roomCode}`   | Check if voted          |
| GET    | `/api/movies/genre/{genre}`           | Get movies by genre     |

---

## 🗺️ Page Flow

```
index.html
  ├── create-room.html → vote.html → result.html → movies.html
  └── join-room.html  → vote.html → result.html → movies.html
```

---

## ✅ Features

- ✅ Create Room with 6-character code
- ✅ Join Room by code
- ✅ Genre voting (12 genres)
- ✅ Vote counting & results
- ✅ Animated result bars
- ✅ TMDB movie recommendations
- ✅ MySQL persistence
- ✅ REST API with validation & error handling

## ⭐ Coming Soon (V2)

- Real-time voting with WebSockets
- Movie poster images
- Trailer button
- Room expiry timer
- Streaming platform filter
