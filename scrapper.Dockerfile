FROM openjdk:21-jdk-slim

WORKDIR /app

COPY root/scrapper/target/scrapper.jar /app/scrapper.jar

EXPOSE 8081

CMD ["java", "-jar", "scrapper.jar"]
