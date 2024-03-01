create table if not exists news
(
    title_url   varchar(255) primary key,
    title       text,
    author_url  text,
    author      text,
    posted_at   text,
    description text,
    added_at    timestamp default current_timestamp
);
