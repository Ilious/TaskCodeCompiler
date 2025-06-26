FROM gradle:8.6-jdk21 AS build

WORKDIR /app

COPY . ./

RUN chmod +x gradlew && ./gradlew :code-compiler-consumer:bootJar --no-daemon --scan -x test

FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app

COPY --from=build /app/code-compiler-consumer/build/libs/*.jar consumer.jar
COPY --from=build /app/code-compiler-consumer/dockerCompiler /app/dockerCompiler

ENTRYPOINT ["java", "-jar", "./consumer.jar"]