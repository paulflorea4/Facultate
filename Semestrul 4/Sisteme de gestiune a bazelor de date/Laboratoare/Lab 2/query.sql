CREATE DATABASE SGBD_Lab2;
GO

USE SGBD_Lab2;

CREATE TABLE employees (
    id INT PRIMARY KEY IDENTITY,
    name NVARCHAR(100),
    salary INT,
    department_id INT
);

INSERT INTO employees (name, salary, department_id)
VALUES 
('Alice', 5000, 1),
('Bob', 6000, 1),
('Charlie', 7000, 5);