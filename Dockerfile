# Build the application
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Run the app with JRE (smaller than JDK)
FROM eclipse-temurin:17-jre-alpine

# Build-time arguments for metadata
ARG APP_VERSION=dev
ARG BUILD_DATE=unknown
ARG COMMIT_SHA=unknown

# Image metadata as labels
LABEL org.opencontainers.image.title="Voluntrix Backend" \
      org.opencontainers.image.description="Backend server for Voluntrix application" \
      org.opencontainers.image.version=${APP_VERSION} \
      org.opencontainers.image.created=${BUILD_DATE} \
      org.opencontainers.image.revision=${COMMIT_SHA} \
      org.opencontainers.image.authors="DevSprint-FIT"

# Install wget for health check
RUN apk add --no-cache wget

# Create a non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -u 1001

WORKDIR /app
# Copy the JAR file from the builder stage
COPY --from=builder /app/target/voluntrix-backend-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appuser app.jar
USER appuser

EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]