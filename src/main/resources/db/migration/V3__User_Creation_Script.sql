CREATE TABLE users (
    username VARCHAR(12) PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(50) NOT NULL
);