CREATE TABLE IF NOT EXISTS mpa (
	id bigserial NOT NULL,
	name varchar(100),
	CONSTRAINT mpa_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films (
	id bigserial NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(200) NOT NULL,
	release_date date NOT NULL,
	duration long,
	mpa_id long,
	CONSTRAINT films_pk PRIMARY KEY (id),
	CONSTRAINT films_fk FOREIGN KEY (mpa_id) REFERENCES mpa(id),
	CONSTRAINT films_uc UNIQUE (name, description)
);

CREATE TABLE IF NOT EXISTS users (
	id bigserial NOT NULL,
	email varchar(50) NOT NULL,
	login varchar(50) NOT NULL,
	name varchar(100) NOT NULL,
	birthday date NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_uc UNIQUE (email, login)
);

CREATE TABLE IF NOT EXISTS friendship (
	friendship_from long,
	friendship_to long,
	CONSTRAINT friendship_pk PRIMARY KEY (friendship_from, friendship_to),
	CONSTRAINT friendship_from_fk FOREIGN KEY (friendship_from) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT friendship_to_fk FOREIGN KEY (friendship_to) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
	film_id long,
	user_id long,
	CONSTRAINT likes_pk PRIMARY KEY (film_id, user_id),
	CONSTRAINT film_id_fk FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS genres (
	id bigserial NOT NULL,
	name varchar(255),
	CONSTRAINT genres_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS film_genres (
	genre_id long,
	film_id long,
	CONSTRAINT film_genres_pk PRIMARY KEY (genre_id, film_id),
	CONSTRAINT genre_id_fk FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT film_id_genre_fk FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE ON UPDATE CASCADE
);





