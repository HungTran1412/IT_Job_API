# Build Java App
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /ITJobAPI
COPY pom.xml .
# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn clean install -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

# Làm việc trong thư mục /app
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /ITJobAPI/target/*.jar ITJobAPI.jar
ENTRYPOINT ["java","-jar","/app/ITJobAPI.jar"]
EXPOSE 8080