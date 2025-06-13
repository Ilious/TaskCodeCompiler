FROM gradle:8.6-jdk21 AS build

WORKDIR /app

COPY . ./

RUN chmod +x gradlew && ./gradlew build --no-daemon --scan -x test

FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

EXPOSE 8080:8080
COPY --from=build /app/build/libs/*.jar backend.jar
COPY --from=build /app/dockerCompiler /app/dockerCompiler

ENTRYPOINT ["java", "-jar", "./backend.jar"]