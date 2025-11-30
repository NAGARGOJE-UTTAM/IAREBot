# --------------------------
# Stage 1: Build the JAR
# --------------------------
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests clean package

# --------------------------
# Stage 2: Run the JAR
# --------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy ANY jar starting with IAREBot and ending with .jar
COPY --from=build /app/target/IAREBot-*-shaded.jar app.jar

CMD ["java", "-jar", "app.jar"]
