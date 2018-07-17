
## Start the server

1. mvn package
2 java -jar target/TFClassFASTA___.jar server config.yml

## Import FASTA files

1. Start the server (see above)
2. Run ```de.sybig.TFClassFASTA.client.InputFasta``` with the parameter ```/path/to/tfclass/dbd/dbd_tree/ /path/to/tfclass/work_tree/```

## Initializing or updating the database

Dropwizard uses Liquibase to maintaine the database. The schema and the updates
are defined in src/main/resources/migration.xml.

To run the (initial) upgrade run ```java -jar target/TFClassFASTA___.jar db migrate config.yml```

More information at: https://www.dropwizard.io/0.7.1/docs/manual/migrations.html
