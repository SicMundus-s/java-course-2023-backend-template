--liquibase formatted sql

--changeset ravenhub:1
CREATE TYPE resource_type AS ENUM ('GITHUB', 'STACKOVERFLOW');

CREATE TABLE links
(
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    url TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_check TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resource_type resource_type NOT NULL,

    PRIMARY KEY (id)
);
--rollback DROP TABLE IF EXISTS links;
