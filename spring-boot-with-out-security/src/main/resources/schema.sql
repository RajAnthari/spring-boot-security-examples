DROP TABLE IF EXISTS books;

CREATE TABLE books (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       published_year INT,
                       price DECIMAL(10,2)
);
