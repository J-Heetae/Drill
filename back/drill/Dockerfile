# Build
FROM openjdk:11-jdk-slim AS builder

WORKDIR /app
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# Production
FROM openjdk:11-jdk-slim

COPY --from=builder /app/build/libs/*.jar app.jar
# EXPOSE 8060

ENTRYPOINT ["java", "-Dspring.profiles.active=prd", "-jar", "/app.jar"]