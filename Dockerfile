FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /w
COPY . .
RUN mvn -q package -DskipTests
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /w/target/deeprag-studio-1.0.0.jar /app.jar
USER app
EXPOSE 8086
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/app.jar"]
