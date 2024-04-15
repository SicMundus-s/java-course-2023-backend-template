FROM openjdk:21-jdk-slim

WORKDIR /app

COPY java-course-2023-backend-template/bot/target/bot.jar /app/bot.jar

EXPOSE 8080

CMD ["java", "-jar", "bot.jar"]
