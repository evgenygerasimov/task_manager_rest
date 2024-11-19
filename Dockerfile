FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/task-manager-rest.jar app.jar
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080