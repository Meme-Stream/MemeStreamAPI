FROM eclipse-temurin:21
WORKDIR /MemeStream
COPY build/libs/MemeStreamAPI-1.0.0.jar .
EXPOSE 8080
CMD ["java", "-jar", "MemeStreamAPI-1.0.0.jar"]