# Build Java App
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /ITJobAPI
COPY pom.xml .
COPY src src
RUN mvn clean install -DskipTests

# Runtime with Tesseract
FROM eclipse-temurin:21-jdk

# Làm việc trong thư mục /app
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /ITJobAPI/target/*.jar ITJobAPI.jar
ENTRYPOINT ["java","-jar","/app/ITJobAPI.jar"]
EXPOSE 8080