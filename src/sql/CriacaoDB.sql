create table usuarios (
                          id serial primary key,
                          username varchar(100) not null,
                          fullname varchar(100) not null,
                          password varchar(100) not null,
                          biography text
);

create table photo (
                       id serial primary key,
                       photo_url text not null,
                       description text,
                       user_id integer not null,
                       foreign key (user_id) references usuarios(id)
);

create table post (
                      id serial primary key,
                      caption text,
                      likes_id integer not null,
                      user_id integer not null,
                      photo_id integer not null,
                      foreign key (user_id) references usuarios(id),
                      foreign key (photo_id) references photo(id),
                      foreign key (likes_id) references likes(id)
);

create table likes (
                       id serial primary key,
                       user_id integer not null,
                       post_id integer not null,
                       foreign key (user_id) references usuarios(id),
                       foreign key (post_id) references post(id)
);

create table user_following (
                                user_id integer not null,
                                following_id integer not null,
                                primary key (user_id, following_id),
                                foreign key (user_id) references usuarios(id),
                                foreign key (following_id) references usuarios(id)
);