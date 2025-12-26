DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS "users" CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS "likes" CASCADE;

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE mpa
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE  films
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    release_date DATE,
    duration     INTEGER CHECK (duration > 0),
    mpa       INTEGER REFERENCES mpa(id)
);

CREATE TABLE  film_genres
(
    film_id  INTEGER NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER NOT NULL REFERENCES genres (id),
    PRIMARY KEY (genre_id, film_id)
);

CREATE TABLE  "users"
(
    id       SERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    login    VARCHAR(255) NOT NULL UNIQUE,
    name     VARCHAR(255) NOT NULL,
    birthday DATE
);

CREATE TABLE  friendship
(
    requester_id INTEGER     NOT NULL REFERENCES "users" (id) ON DELETE CASCADE,
    addressee_id INTEGER     NOT NULL REFERENCES "users" (id) ON DELETE CASCADE,
    PRIMARY KEY (requester_id, addressee_id),
    status       VARCHAR(10) NOT NULL
);

CREATE TABLE  "likes"
(
    film_id INTEGER NOT NULL REFERENCES films (id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES "users" (id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, user_id)
);







