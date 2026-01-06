FROM eclipse-temurin:24-jre
COPY ./build/libs/forwarder-0.0.1-SNAPSHOT.jar Forwarder.jar
ENTRYPOINT ["java","-jar","/Forwarder.jar"]
