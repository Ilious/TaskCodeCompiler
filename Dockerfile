FROM gradle:8.6-jdk21 AS build

WORKDIR /app

COPY ./build.gradle ./settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

EXPOSE 8080:8080
COPY --from=build /app/build/libs/*.jar backend.jar
ENTRYPOINT ["java", "-jar", "./backend.jar"]