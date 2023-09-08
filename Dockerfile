FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN chmod +x /app/gradlew
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/com.example.productive-app-all.jar"]
EXPOSE 8080