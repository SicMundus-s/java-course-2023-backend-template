FROM openjdk:21-jdk-slim

WORKDIR /app

COPY root/bot/target/bot.jar /app/bot.jar

EXPOSE 8080

CMD ["java", "-jar", "bot.jar"]
