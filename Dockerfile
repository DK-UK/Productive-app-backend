FROM openjdk:latest
COPY . /app
WORKDIR /app
ENTRYPOINT ["gradle"]
CMD ["build", "--stacktrace"]
CMD ["java", "-jar", "build/libs/com.example.productive-app-all.jar"]
EXPOSE 8080
