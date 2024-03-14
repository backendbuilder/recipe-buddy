# Readme
## Recipe-buddy

### Steps to run the application

1. Make sure you have a running instance of Docker and Docker-compose. I'm using Docker Desktop which had both included.
    1. to check if you have Docker installed use the following command. I have Docker version 24.0.5 installed.
   ```bash
     docker -v 
    ```
    2. to check if you have Docker-compose installed use the following command. I have Docker Compose version v2.20.2-desktop.1 installed.
   ```bash
   docker-compose -v
    ```
2. Now you can run recipe-buddy and a containerized database  with a single Docker-compose command
    ```bash
        docker compose  up --build 
    ```
   1. the "--build" flag makes sure all the jar files are build before creating images of them and running the containers.

3. visit Swagger-UI in your internet browser, to see AND use the available API's
    ```bash
        http://localhost:8080/swagger-ui.html
    ```


