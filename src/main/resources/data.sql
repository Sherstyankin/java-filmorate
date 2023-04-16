--наполнение таблицы genre
INSERT INTO genres (name) VALUES ('Комедия'); -- 1
INSERT INTO genres (name) VALUES ('Драма'); -- 2
INSERT INTO genres (name) VALUES ('Мультфильм'); -- 3
INSERT INTO genres (name) VALUES ('Триллер'); -- 4
INSERT INTO genres (name) VALUES ('Документальный'); -- 5
INSERT INTO genres (name) VALUES ('Боевик'); -- 6

--наполнение таблицы mpa
INSERT INTO mpa (name) VALUES ('G'); -- 1 "кино без всяких возрастных ограничений"
INSERT INTO mpa (name) VALUES ('PG'); -- 2 "маленьким детям рекомендуется просмотр с родителями"
INSERT INTO mpa (name) VALUES ('PG-13'); -- 3 "детям до 13 просмотр не желателен"
INSERT INTO mpa (name) VALUES ('R'); -- 4 "зрители до 17 лет должны присутствовать в зале с сопровождением родителей"
INSERT INTO mpa (name) VALUES ('NC-17'); -- 5 "лицам до 18 лет просмотр запрещён"