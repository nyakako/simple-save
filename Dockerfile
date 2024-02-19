FROM maven:3-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -Dmaven.test.skip=true
FROM eclipse-temurin:21-alpine
COPY --from=build /target/[simplesave-0.0.1-SNAPSHOT].jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]