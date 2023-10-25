FROM azul/zulu-openjdk-alpine:11-latest AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew build

FROM azul/zulu-openjdk-alpine:11-latest
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8060
ENTRYPOINT ["java","-jar","/app.jar"]
