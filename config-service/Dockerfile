FROM eclipse-temurin:25
WORKDIR /app
COPY target/*.jar app.jar
COPY src/main/resources/config /config/
ENTRYPOINT ["java", "-jar", "app.jar"]
