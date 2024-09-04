FROM eclipse-temurin:21
WORKDIR /MemeStream
COPY build/libs/MemeStreamAPI-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "MemeStreamAPI-0.0.1-SNAPSHOT.jar"]