# Bet Fanatics Coding Challenge

This challenge was solved by implementing an API that will execute the steps outlined in the coding challenge.
The application is a Spring Boot API and Maven is used to manage dependencies. The application is written in Java 17.

## Running the Application
You can start the application either by running a standalone JAR, using the Maven plugin or using Docker.

#### Standalone Jar

1) Package the application using maven to create an executable jar: `mvn clean install`
2) Run the jar: `java -jar target/coding-challenge-0.0.1-SNAPSHOT.jar`

#### Maven Plugin
Note: Must be using Java 17 to run the application using Maven Plugin

To run the application using maven run `mvn spring-boot:run`

#### Docker Container

1) Build the image from root: `docker build -t coding-challenge . `
2) Run docker container bind to port 8080: `docker run -p 8080:8080 coding-challenge
   `
## Trying it out

In order for the worfklow to be executed you must make a `POST` request to: `http://localhost:8080/api/v1/execute`. You must add an `Authorization` header with a Bearer Token retrieved from [Go Rest](https://gorest.co.in/consumer/login).

Example: `Authorization`, `Bearer <access-token>`

Once the workflow is executed successfully you will receive the following response "Successfully completed workflow". If the token is missing or invalid you will receive a message regarding a client exception and asking for valid token.

