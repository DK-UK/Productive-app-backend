FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN chmod +x gradlew
RUN ./gradlew build --stacktrace
CMD ["java", "-jar", "build/libs/com.example.productive-app-all.jar"]
EXPOSE 8080