FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
WORKDIR /app
COPY /target/maven-archiver/* archiver/
ENTRYPOINT ["java", "-cp", "/app/archiver/*", "-Dloader.main=com.savannasolutions.SmartContractVerifierServer.SmartContractVerifierServerApplication.kt", "org.springframework.boot.loader.PropertiesLauncher"]
EXPOSE 8080