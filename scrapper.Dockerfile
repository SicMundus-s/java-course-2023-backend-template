FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/scrapper-app.jar /app/scrapper-app.jar

EXPOSE 8081

CMD ["java", "-jar", "scrapper-app.jar"]
