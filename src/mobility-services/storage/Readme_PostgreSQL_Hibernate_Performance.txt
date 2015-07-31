In order to overcome the issues with the connection pool of hibernate,
we setup the PostgreSQL to have a maximum number of 10000 connections.
This allowed for inserting between 200 and 300 entries in a role
without having the storage getting stuck in a deadlock.
In addition, the database server needs to be regularly restarted and 
the storage redeployed, in order to enable the continuous and stable
storage of data.