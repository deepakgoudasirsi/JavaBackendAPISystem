-- Database initialization script for Java Backend API System
-- This script sets up the initial database structure and sample data

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS backend_api_db;
USE backend_api_db;

-- Create user if not exists
CREATE USER IF NOT EXISTS 'api_user'@'%' IDENTIFIED BY 'api_password';
GRANT ALL PRIVILEGES ON backend_api_db.* TO 'api_user'@'%';
FLUSH PRIVILEGES;

-- The tables will be created automatically by Hibernate/JPA
-- This script is mainly for Docker initialization

-- Sample data can be inserted here if needed
-- INSERT INTO users (username, email, password, first_name, last_name, role, is_active, created_at, updated_at) 
-- VALUES ('admin', 'admin@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'ADMIN', true, NOW(), NOW());
