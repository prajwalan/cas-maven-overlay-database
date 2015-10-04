# cas-maven-overlay-database
A sample maven overlay based CAS server that authenticates against the users in a database. Configuration of database is specified in the config.properties file in resources folder. This coud be read from an external location also.

Most important part here is the DatabaseAuthenticationHandler class that is in the same package as rest of the other handlers.
