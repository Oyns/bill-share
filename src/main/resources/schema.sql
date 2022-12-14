DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS parties CASCADE;
DROP TABLE IF EXISTS users_party CASCADE;
DROP TABLE IF EXISTS items_party CASCADE;
DROP TABLE IF EXISTS users_items CASCADE;


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id        uuid,
    user_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS users_index ON users (id);

CREATE TABLE IF NOT EXISTS items
(
    id         uuid,
    item_name  VARCHAR(255) NOT NULL,
    price      DECIMAL,
    amount     INTEGER,
    equally    BOOLEAN,
    discount   DECIMAL,
    user_id    uuid,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_items PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS items_index ON items (id);

CREATE TABLE IF NOT EXISTS parties
(
    id           uuid,
    party_name   VARCHAR(255) NOT NULL,
    paid         BOOLEAN,
    initiator_id uuid,
    CONSTRAINT pk_parties PRIMARY KEY (id),
    CONSTRAINT fk_users FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS parties_index ON parties (id);

CREATE TABLE IF NOT EXISTS users_party
(
    party_id uuid,
    user_id  uuid,
    CONSTRAINT pk_users_party PRIMARY KEY (party_id, user_id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_parties FOREIGN KEY (party_id) REFERENCES parties (id)
);

CREATE TABLE IF NOT EXISTS items_party
(
    party_id uuid,
    item_id  uuid,
    CONSTRAINT pk_items_party PRIMARY KEY (party_id, item_id),
    CONSTRAINT fk_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_parties FOREIGN KEY (party_id) REFERENCES parties (id)
);

CREATE TABLE IF NOT EXISTS users_items
(
    user_id uuid,
    item_id uuid,
    CONSTRAINT pk_users_items PRIMARY KEY (user_id, item_id),
    CONSTRAINT fk_items FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id)
);