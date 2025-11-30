# --------------------------
# Stage 1: Build the JAR
# --------------------------
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -e -DskipTests clean package

# --------------------------
# Stage 2: Run the JAR
# --------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/IAREBot-1.0-SNAPSHOT-shaded.jar app.jar

CMD ["java", "-jar", "app.jar"]
