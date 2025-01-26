-- Q&A MySQL Schema
CREATE DATABASE IF NOT EXISTS QnADatabase;

USE QnADatabase;

-- Table for storing questions and answers
CREATE TABLE QnA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP NULL
);

-- Example user credentials for the database
-- Ensure to change the username and password in your application settings
CREATE USER 'qna_user'@'localhost' IDENTIFIED BY 'securepassword';
GRANT ALL PRIVILEGES ON QnADatabase.* TO 'qna_user'@'localhost';
FLUSH PRIVILEGES;
