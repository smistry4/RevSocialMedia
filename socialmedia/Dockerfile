FROM openjdk:21-jre-slim
WORKDIR /app
COPY socialmedia/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT [ "java","-jar","app.jar" ]