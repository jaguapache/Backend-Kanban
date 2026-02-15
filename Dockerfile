# Etapa de build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

# Etapa de runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/kanban-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]