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

    $ 


## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

## License

Copyright © 2015 FIXME
