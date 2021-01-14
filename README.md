Class Diary ReadMe
This is web client.

Building and running this application without docker
Run the below maven command

mvn clean spring-boot:run -Dspring.profiles.active=dev

With Docker - App, PostgreSQL ,Elastic search
References
Docker Compose for Spring Boot application with PostgreSQL
Package the application
Package the application
$ mvn clean package

To skip the tests use: -DskipTests=true

Extract libraries from fat-jar
$ mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

Run
$ docker-compose build && docker-compose up

Verify the application is running
Application listens on port 80.