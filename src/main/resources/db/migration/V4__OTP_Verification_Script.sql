CREATE TABLE otp_verification (
    username VARCHAR(12) PRIMARY KEY,
    otp VARCHAR(6) NOT NULL,
    expiry_time TIMESTAMP NOT NULL
);