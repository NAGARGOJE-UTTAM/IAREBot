IAREBot
/Dockerfile
NAGARGOJE-UTTAM
NAGARGOJE-UTTAM
Add Dockerfile for IAREBot deployment
b1714d2
 · 
28 minutes ago
8 lines (6 loc) · 180 Bytes

Code

Blame
Code view is read-only.
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the shaded jar created by Maven
COPY target/IAREBot-1.0-SNAPSHOT-shaded.jar app.jar

# Run the bot
CMD ["java", "-jar", "app.jar"]
