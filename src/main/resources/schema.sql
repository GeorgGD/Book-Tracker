DROP TABLE IF EXISTS library;

CREATE TABLE IF NOT EXISTS library (
       id INT NOT NULL,
       name VARCHAR(200) NOT NULL,
       author VARCHAR(200),
       genre VARCHAR(300),
       reading TINYINT(1),
       completed_date DATE,
       cover_img TEXT,
       PRIMARY KEY (id)
);

