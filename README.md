# Admin - Audit Log
Maxx restful service, now support Postgresql only. 
## Setup
### Database 
Before starting application, please run below commands to create user and create database.
```bash
createuser -U postgres --createdb -e --connection-limit=50 --pwprompt maxxsys
```
To run, we should know database super user postgres password.

```bash
createdb --owner=maxxsys --encoding=utf-8  --username=maxxsys -e maxxdb
```
To run, we should know database user maxxsys password.

### Database profile
After successfully create database and role, following profiles are supported.

#### 1. db-migrate
db-migrate will let flyway execute all database scripts under db/migration in classpath to Postgresql, and migrate database to latest status.
Sample command:
```bash
java -jar -Dspring.profiles.active=db-migrate target/maxx-restful-service-${version}-executable.jar
```
#### 2. ${client}-development
${client}-development will trigger development profile and related configuration defined in application.yaml
Sample command:
``` bash
java -jar -Dspring.profiles.active=uob-development target/maxx-restful-service-${version}-executable.jar
```

#### 3. ${client}-production
${client}-production will trigger production profile and related configuration defined in application.yaml
Sample command:
```bash
java -jar -Dspring.profiles.active=uob-production target/maxx-restful-service-${version}-executable.jar
```

## How to run
```bash
mvn clean install
```
To build the project

```bash
mvn integration-test
# or 
mvn verify
```
will trigger cucumber to run Integration test and Unit test

```bash
mvn test
```
will trigger Unit test

```bash
mvn spring-boot:run
```
will start the server
You can visit the web server at ```http://localhost:9000/auditlog``` or ```http://localhost:9000/```

## Note
1. Maxx restful service support start only from JDK 8
2. actuator is enabled and used to track system health as a monitoring tool. Sample url is /health, /trace, and you can check Spring doc to get more information.
3. To read more about Spring Boot http://projects.spring.io/spring-boot/
4. To debug the application, add a debug configuration Maven add working directory ```.../Admin-AuditLog``` and command line as ```spring-boot:run```

## Troubleshooting
Issues frequently encountered:
 - ```org.hibernate.MappingException: Unknown entity``` check ```DatabaseConfig.java``` and ```http://www.baeldung.com/hibernate-mappingexception-unknown-entity```
 - Database does not update on Enum column, add ```@Enumerated(EnumType.STRING)```
