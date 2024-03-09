--liquibase formatted sql

--changeset ravenhub:1
CREATE TABLE chats
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (chat_id)
);
--rollback DROP TABLE IF EXISTS chats;

--changeset ravenhub:2
CREATE TABLE links
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    url TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,


    PRIMARY KEY (id),
    UNIQUE (url)
);
--rollback DROP TABLE IF EXISTS links;

--changeset ravenhub:3
CREATE TABLE chat_links
(
    chat_id BIGINT NOT NULL,
    link_id BIGINT NOT NULL,

    PRIMARY KEY (chat_id, link_id),

    FOREIGN KEY (chat_id) REFERENCES chats (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (link_id) REFERENCES links (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
--rollback DROP TABLE IF EXISTS chat_links;
