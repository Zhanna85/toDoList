DROP TABLE IF EXISTS users, tasks, subtasks CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    email varchar(254) NOT NULL UNIQUE,
    name varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    type varchar(4) NOT NULL DEFAULT 'TASK',
    name varchar(2000) NOT NULL,
    description varchar(7000) NOT NULL,
    status varchar(255) NOT NULL DEFAULT 'NEW',
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS subtasks (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    type varchar(4) NOT NULL DEFAULT 'SUBTASK',
    name varchar(2000) NOT NULL,
    description varchar(7000) NOT NULL,
    status varchar(255) NOT NULL DEFAULT 'NEW',
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    task_id bigint NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    initiator_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL
    );