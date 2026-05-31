-- =============================================
-- Movie Night Decision Maker - Database Schema
-- =============================================

CREATE DATABASE IF NOT EXISTS movie_night_db;
USE movie_night_db;

-- Room Table
CREATE TABLE IF NOT EXISTS room (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_code VARCHAR(6) NOT NULL UNIQUE,
    room_name VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_room_code (room_code)
);

-- Participant Table
CREATE TABLE IF NOT EXISTS participant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    room_id BIGINT NOT NULL,
    CONSTRAINT fk_participant_room FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE,
    INDEX idx_participant_room (room_id)
);

-- Vote Table
CREATE TABLE IF NOT EXISTS vote (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    genre VARCHAR(50) NOT NULL,
    participant_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    CONSTRAINT fk_vote_participant FOREIGN KEY (participant_id) REFERENCES participant(id) ON DELETE CASCADE,
    CONSTRAINT fk_vote_room FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE,
    UNIQUE KEY uq_participant_room_vote (participant_id, room_id),
    INDEX idx_vote_room (room_id)
);
