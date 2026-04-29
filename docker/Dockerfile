FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app
COPY gradlew .
RUN sed -i 's/\r$//' gradlew
RUN chmod +x gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon
COPY src src
#RUN ./gradlew spotlessApply
RUN ./gradlew clean build -x test -x spotlessJavaCheck

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]