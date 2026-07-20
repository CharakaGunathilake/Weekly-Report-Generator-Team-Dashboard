FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Copy only the dependency files first to leverage Docker layer caching
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the actual source code
COPY src ./src

# Build the application package (skipping tests for faster production builds)
RUN ./mvnw package -DskipTests

# --- Stage 2: The Production Runtime ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Create a non-root system user for security hardening
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy only the compiled JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application with optimized JVM flags
ENTRYPOINT ["java", "-jar", "app.jar"]