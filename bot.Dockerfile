FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/bot-app.jar /app/bot-app.jar

EXPOSE 8080

CMD ["java", "-jar", "bot-app.jar"]
