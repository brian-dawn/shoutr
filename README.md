# shoutr

Master brach: [![Build Status](https://travis-ci.org/lambdas-of-love/shoutr.svg?branch=master)](https://travis-ci.org/lambdas-of-love/shoutr)

## Getting started

### 

### Creating the Postgres Databases

Create the databases and the shoutr user:

    $ createdb shoutr_dev
    $ createdb shoutr_test
    $ createuser shoutr
    $ psql
    # ALTER USER shoutr WITH SUPERUSER;
    # ALTER USER shoutr WITH PASSWORD 'shoutr';

Now we can run the database migrations:

    $ lein run migrate

If we ever need to rollback a migration:

    $ lein run rollback

### Creating a new migration

    $ lein migratus create add-users-table

This will give us a new migration file we can dump SQL into.

### Running the app:

The app will autoload any changes made to the files. Running it for development is a two part
process, first run the main Clojure app:

    $ lein run

In a separate shell we'll need to run figwheel to compile the ClojureScript:

    $ lein figwheel

### App Overview:

    profiles.clj - Contains environment information such as JDBC connection info.
    Procfile     - Contains information used to run this app on Dokku (or Heroku).

### SQL:

SQL queries not used as part of a migration can go into:

    resources/sql

The files will contain the queries themselves along with some metadata in the SQL comments
to automatically create corresponding Clojure functions. Example:

```sql
-- name: get-user
-- retrieve a user given the id.
SELECT * FROM users
WHERE user_uid = :user_uid
```

Will automatically create a Clojure function `get-user`. The SQL query contains the keyword `:user_uid` which
acts as a parameter to be filled in later.

To call it from Clojure:

```clojure
;; Require [shoutr.db.core :as db]
(db/get-user {:user_uid uuid})
```

The functions automatically created from the query take a map of the keys to be filled in on the query.

## License

Copyright © 2015 FIXME
