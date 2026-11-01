CREATE DATABASE IF NOT EXISTS primedb;
USE primedb;

CREATE TABLE IF NOT EXISTS prime_store (
    n         INT PRIMARY KEY,
    nth_prime INT NOT NULL
);
