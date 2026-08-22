FROM eclipse-temurin:25
WORKDIR /app
COPY target/*.jar app.jar
RUN mkdir -p /app/logs
ENTRYPOINT ["java", "-jar", "app.jar"]