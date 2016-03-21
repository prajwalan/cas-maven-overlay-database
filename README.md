# cas-maven-overlay-database
A sample maven overlay based [CAS](http://jasig.github.io/cas/4.1.x/index.html) server that authenticates against the users in a database. Configuration of database is specified in the config.properties file in resources folder. This coud be read from an external location also.

Most important parts here are:
- The DatabaseAuthenticationHandler class that is in the same package as rest of the other handlers.
- And the SearchModeSearchDatabaseAuthenticationHandler bean configuration in deployerConfigContext.xml.
