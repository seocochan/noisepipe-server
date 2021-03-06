FROM openjdk:8-jre

COPY ./target/noisepipe-server.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]