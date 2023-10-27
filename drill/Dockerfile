# Build
FROM openjdk:11 AS builder

COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew build -x test

# Production
FROM openjdk:11
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java","-jar","/app.jar"]