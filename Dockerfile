FROM openjdk:latest
COPY . /app
WORKDIR /app
RUN apk update && apk add xargs
RUN chmod +x gradlew
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/com.example.productive-app-all.jar"]
EXPOSE 8080