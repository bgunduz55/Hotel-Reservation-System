# Multi-stage build for Hotel Service
FROM eclipse-temurin:17-jdk AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy entire project
COPY . .

# Build only hotel-service
RUN mvn clean package -DskipTests -pl hotel-service

# Runtime stage
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Create app user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built jar from builder stage
COPY --from=builder /app/hotel-service/target/*.jar app.jar

# Create necessary directories
RUN mkdir -p /app/logs && chown -R appuser:appuser /app

# Switch to app user
USER appuser

# Expose port
EXPOSE 8081

# Set JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 