-- tables
-- Table: service
CREATE TABLE services (
    service_uid UUID NOT NULL,
    service_type int  NOT NULL, --don't know if this should be an int, was thinking this would have a mapping to some kind of enum in the code
    CONSTRAINT service_pk PRIMARY KEY (service_uid)
);
--;;

-- Table: service_users
CREATE TABLE service_users (
    service_user_uid UUID NOT NULL, --could also use (user_uid, service_uid) as PK
    user_uid UUID NOT NULL,
    service_uid UUID NOT NULL,
    api_key text  NOT NULL,
    CONSTRAINT service_users_uniq_user_uid_service_uid UNIQUE (user_uid, service_uid) NOT DEFERRABLE  INITIALLY IMMEDIATE ,
    CONSTRAINT service_users_pk PRIMARY KEY (service_user_uid)
);
--;;

-- Table: users
CREATE TABLE users (
    user_uid UUID NOT NULL,
    username varchar(20)  NOT NULL,
    password varchar(64)  NOT NULL,
    email varchar(100) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_uid)
);
--;;

-- foreign keys
-- Reference:  service_users__services (table: service_users)
ALTER TABLE service_users ADD CONSTRAINT service_users__services
    FOREIGN KEY (service_uid)
    REFERENCES services (service_uid)
    ON DELETE  CASCADE
    ON UPDATE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;
--;;

-- Reference:  service_users__users (table: service_users)
ALTER TABLE service_users ADD CONSTRAINT service_users__users
    FOREIGN KEY (user_uid)
    REFERENCES users (user_uid)
    ON DELETE  CASCADE
    ON UPDATE  CASCADE
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;
--;;
-- End of file.
