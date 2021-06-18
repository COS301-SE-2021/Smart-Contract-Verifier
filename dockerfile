FROM adoptopenjdk/openjdk11:jdk-11.0.9.1_1-alpine-slim
WORKDIR /app
COPY build/libs libs/
COPY build/resources resources/
COPY build/classes classes/
ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "-Dloader.main=com.myapp.mainkt", "org.springframework.boot.loader.PropertiesLauncher"]
EXPOSE 8080