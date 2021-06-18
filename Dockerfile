FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
RUN apk update && apk add maven
WORKDIR /docker
RUN mvn package
ENTRYPOINT ["java", "-jar", "Smart-Contract-Verifier-Server-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080