Maxx restful service now support Postgresql only. Before starting application, please run below commands to create user and create database.
1) createuser -U postgres --createdb -e --connection-limit=50 --pwprompt maxxsys
2) createdb --owner=maxxsys --encoding=utf-8  --username=maxxsys -e maxxdb

Please note that although we built and source level of Maxx restful service is 1.6, Maxx restful service must run on JDK8 now. We will migrate
Maxx restful service to JDK 8 eventually.
To run command 1, we should know database super user postgres password. To run command 2, we should know database user maxxsys password.

After successfully create database and role, following profiles are supported.

1) db-migrate
db-migrate will let flyway execute all database scripts under db/migration in classpath to Postgresql, and migrate database to latest status.
Sample command:
java -jar -Dspring.profiles.active=db-migrate target/maxx-restful-service-${version}-executable.jar

2) ${client}-development
${client}-development will trigger development profile and related configuration defined in application.yaml
Sample command:
java -jar -Dspring.profiles.active=uob-development target/maxx-restful-service-${version}-executable.jar

3) ${client}-production
${client}-production will trigger production profile and related configuration defined in application.yaml
Sample command:
java -jar -Dspring.profiles.active=uob-production target/maxx-restful-service-${version}-executable.jar

Note:
1) Maxx restful service support start only from JDK 8
2) mvn integration-test or mvn verify will trigger cucumber to run integration test as well as unit test case.
3) mvn test will only trigger unit test.
4) actuator is enabled and used to track system health as a monitoring tool. Sample url is /health, /trace, and you can check Spring doc to get more information.
