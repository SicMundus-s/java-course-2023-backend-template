FROM openjdk:21-jdk-slim

WORKDIR /app

COPY java-course-2023-backend-template/scrapper/target/scrapper.jar /app/scrapper.jar

EXPOSE 8081

CMD ["java", "-jar", "scrapper.jar"]
