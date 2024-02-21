FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-alpine
COPY --from=build /target/simplesave-0.0.1-SNAPSHOT.jar simplesave.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xms256m", "-Xmx512m", "-jar", "simplesave.jar"]