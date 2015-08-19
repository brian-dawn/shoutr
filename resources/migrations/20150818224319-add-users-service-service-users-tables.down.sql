-- foreign keys
ALTER TABLE service_users DROP CONSTRAINT service_users__services;
--;;
ALTER TABLE service_users DROP CONSTRAINT service_users__users;
--;;

-- tables
DROP TABLE services;
--;;

DROP TABLE service_users;
--;;

DROP TABLE users;
--;;
-- End of file.
