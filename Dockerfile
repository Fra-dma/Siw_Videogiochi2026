# Stage 1: Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the application, skipping tests as they will be run in CI
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built jar from the build stage
COPY --from=build /app/target/Progetto_SIW-1.0.jar app.jar
# Render routes external traffic to the port we expose
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
