
CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS public.users
(
    id            BIGINT      NOT NULL,
    login         VARCHAR(50) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    app_user_role VARCHAR(50),
    CONSTRAINT pk_users PRIMARY KEY (id)
);
