FROM openjdk:21-jdk

WORKDIR /app

COPY target/Blog-Application-AI-Integration-0.0.1-SNAPSHOT.jar Blog-Application-AI-Integration-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","Blog-Application-AI-Integration-0.0.1-SNAPSHOT.jar"]