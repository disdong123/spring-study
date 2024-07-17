USE reactive_study;

CREATE USER IF NOT EXISTS 'test'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON reactive_study.* TO 'test'@'localhost';
FLUSH PRIVILEGES;