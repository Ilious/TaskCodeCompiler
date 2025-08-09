FROM gradle:8.6-jdk21 AS build

WORKDIR /app

COPY . ./

RUN chmod +x gradlew && ./gradlew :code-compiler-api:bootJar --no-daemon --scan -x test

FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

COPY --from=build /app/code-compiler-api/build/libs/*.jar server.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "./server.jar"]