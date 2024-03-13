FROM gradle:jdk17 as builder

COPY --chown=gradle:gradle . /app

WORKDIR /app

RUN gradle bootJar

FROM amazoncorretto:17

# Copy the application by hardcoded file name, TODO always build latest version, version msut be not hard coded
COPY --from=builder /app/build/libs/recipe-buddy-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
