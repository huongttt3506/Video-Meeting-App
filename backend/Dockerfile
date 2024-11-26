# Use official OpenJDK image from Docker Hub
FROM eclipse-temurin:17 AS build

# Set working directory
WORKDIR /app

# Copy the build configuration
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build the app
RUN ./gradlew bootJar
RUN mv build/libs/*.jar app.jar

# New Stage to copy built jar into a smaller runtime container
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar file from the build container to the runtime container
COPY --from=build /app/app.jar .

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]

EXPOSE 8080