--liquibase formatted sql

--changeset ravenhub:1
ALTER TABLE links
    ADD COLUMN github_updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;
--rollback DROP COLUMN github_pushed_at

--changeset ravenhub:2
ALTER TABLE links
    ADD COLUMN stackoverflow_last_edit_date_question TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;
--rollback DROP COLUMN stackoverflow_last_edit_date_question
