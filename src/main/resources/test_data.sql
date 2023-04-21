--наполнение таблицы users
INSERT INTO users (email, login, name, birthday) --1
VALUES ('greg@mail.com', 'grey', 'Greg', '2000-01-01');

INSERT INTO users (email, login, name, birthday) --2
VALUES ('alex@mail.com', 'axe', 'Alex', '2001-03-02');

INSERT INTO users (email, login, name, birthday) --3
VALUES ('nick@mail.com', 'nickname', 'Nick', '2005-02-13');

--наполнение таблицы films
INSERT INTO films (name, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) --1
VALUES ('Терминатор', 'Робот из будущего', '1991-01-01', 130, 4);

INSERT INTO films (name, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) --2
VALUES ('Пятый элемент', 'Спасение Земли', '1997-01-01', 125, 3);

INSERT INTO films (name, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) --3
VALUES ('Терминал', 'В аэропорту', '2004-01-01', 115, 2);
