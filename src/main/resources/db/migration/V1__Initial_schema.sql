create table teams(
    id bigserial not null primary key,
    name varchar(200) not null,
    archived boolean not null,
    created_date timestamp not null,
    last_modified_date timestamp not null,
    creator_id varchar,
    last_modified_by varchar not null,
    version bigint not null

);

create table boards
(
    id          bigserial not null primary key ,
    name        varchar   not null,
    description varchar,
    team_id     bigint    not null ,
    archived    boolean,
    created_date timestamp not null,
    last_modified_date timestamp not null,
    creator_id  varchar   not null ,
    last_modified_by varchar not null,
    version bigint not null
);

create table board_members
(
    id bigserial not null primary key ,
    board_id bigint    not null references boards(id) on delete cascade,
    user_id  varchar   not null,
    unique (board_id, user_id)
);
