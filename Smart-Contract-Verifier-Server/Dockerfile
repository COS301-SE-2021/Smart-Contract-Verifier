FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim

ADD /src/main/resources/application.properties application.properties
ADD /target/Smart-Contract-Verifier-Server-0.0.1-SNAPSHOT.jar UnisonServer.jar

RUN ["mkdir", "UnisonEvidence"]

ENTRYPOINT ["java", "-jar", "UnisonServer.jar"]

EXPOSE 8080