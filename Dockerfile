FROM openjdk:latest
COPY . /app
WORKDIR /app
chmod +x gradlew
RUN ./gradlew.bat build
CMD ["java", "-jar", "build/libs/com.example.productive-app-all.jar"]
EXPOSE 8080