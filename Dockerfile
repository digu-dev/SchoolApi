FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests



FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY ./target/SchoolAPI-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java", "-jar", "SchoolAPI-0.0.1-SNAPSHOT.jar"]