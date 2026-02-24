FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests -B -q

FROM amazoncorretto:21-al2023
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/uploads /app/logs

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
