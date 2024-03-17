--liquibase formatted sql

--changeset ravenhub:1
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
