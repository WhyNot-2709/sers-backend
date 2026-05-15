FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Djava.net.preferIPv4Stack=true","-jar","/app.jar"]
