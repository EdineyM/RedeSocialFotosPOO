create database redesocial;
\c redesocial;

create table usuarios (
                          id serial primary key,
                          username varchar(100) not null,
                          fullname varchar(100) not null,
                          password varchar(100) not null,
                          biography text
);

create table photo (
                       id serial primary key,
                       photo_url bytea not null,
                       description text,
                       user_id integer not null,
                       foreign key (user_id) references usuarios(id)
);

CREATE TABLE post (
                      id SERIAL PRIMARY KEY,
                      caption TEXT,
                      user_id INTEGER NOT NULL,
                      photo_id INTEGER NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES usuarios(id),
                      FOREIGN KEY (photo_id) REFERENCES photo(id)
);

CREATE TABLE likes (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER NOT NULL,
                       post_id INTEGER NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES usuarios(id),
                       FOREIGN KEY (post_id) REFERENCES post(id)
);

ALTER TABLE post
    add COLUMN likes_id INTEGER,
    ADD CONSTRAINT fk_likes_id
        FOREIGN KEY (likes_id) REFERENCES likes(id);

create table user_following (
                                user_id integer not null,
                                following_id integer not null,
                                primary key (user_id, following_id),
                                foreign key (user_id) references usuarios(id),
                                foreign key (following_id) references usuarios(id)
);

ALTER TABLE post
    drop constraint fk_likes_id;