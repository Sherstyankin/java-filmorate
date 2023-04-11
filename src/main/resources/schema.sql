CREATE TABLE IF NOT EXISTS mpa (
	id serial NOT NULL,
	name varchar(255),
	CONSTRAINT mpa_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films (
	id serial NOT NULL,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	release_date date NOT NULL,
	duration int,
	mpa_id int,
	CONSTRAINT films_pk PRIMARY KEY (id),
	CONSTRAINT films_fk FOREIGN KEY (mpa_id) REFERENCES mpa(id)
);

CREATE TABLE IF NOT EXISTS users (
	id serial NOT NULL,
	email varchar(255) NOT NULL,
	login varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	birthday date NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS friendship (
	friendship_from int,
	friendship_to int,
	CONSTRAINT friendship_pk PRIMARY KEY (friendship_from, friendship_to),
	CONSTRAINT friendship_from_fk FOREIGN KEY (friendship_from) REFERENCES users(id),
	CONSTRAINT friendship_to_fk FOREIGN KEY (friendship_to) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS likes (
	film_id int,
	user_id int,
	CONSTRAINT likes_pk PRIMARY KEY (film_id, user_id),
	CONSTRAINT film_id_fk FOREIGN KEY (film_id) REFERENCES films(id),
	CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS genres (
	id serial NOT NULL,
	name varchar(255),
	CONSTRAINT genres_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film_genres (
	genre_id int,
	film_id int,
	CONSTRAINT film_genres_pk PRIMARY KEY (genre_id, film_id),
	CONSTRAINT genre_id_fk FOREIGN KEY (genre_id) REFERENCES genres(id),
	CONSTRAINT film_id_genre_fk FOREIGN KEY (film_id) REFERENCES films(id)
);





