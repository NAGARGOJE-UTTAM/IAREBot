FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the shaded jar created by Maven
COPY target/IAREBot-1.0-SNAPSHOT-shaded.jar app.jar

# Run the bot
CMD ["java", "-jar", "app.jar"]
