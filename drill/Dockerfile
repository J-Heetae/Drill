# # Build
# FROM openjdk:11-jdk-slim AS builder

# WORKDIR /app
# COPY build.gradle .
# COPY settings.gradle .
# COPY gradle.properties .
# COPY src/ src/

# RUN chmod +x ./gradlew
# RUN ./gradlew build -x test

# # Production
# FROM openjdk:11-jdk-slim

# COPY --from=builder /app/build/libs/*.jar app.jar
# EXPOSE 8060

# ENTRYPOINT ["java", "-Dspring.profiles.active=prd", "-jar", "/app.jar"]


# OpenJDK 11을 베이스로 하는 이미지를 사용합니다. 'slim'은 축소된 이미지로 필요한 최소한의 패키지만 포함합니다.
FROM openjdk:11-jdk-slim

# JAR_FILE이라는 변수를 선언하고, 디폴트로 'build/libs/*.jar'을 값으로 설정합니다.
# 이것은 빌드된 JAR 파일의 위치를 나타냅니다.
ARG JAR_FILE=build/libs/*.jar

# 지정된 JAR 파일을 'app.jar'라는 이름으로 컨테이너 내부에 복사합니다.
COPY ${JAR_FILE} app.jar

# 컨테이너의 8200 포트를 열어 외부와의 통신이 가능하게 합니다.
EXPOSE 8200

# 컨테이너가 실행될 때 'java -jar app.jar' 명령을 실행하여 애플리케이션을 구동합니다.
ENTRYPOINT ["java", "-Dspring.profiles.active=prd", "-jar", "app.jar"]