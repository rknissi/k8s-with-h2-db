FROM openjdk:8-jdk-alpine

COPY ./target/demo-h2-0.0.1-SNAPSHOT.jar demoh2.jar

ENTRYPOINT ["java", "-jar", "demoh2.jar", "-Dspring.profiles.active=prod"]