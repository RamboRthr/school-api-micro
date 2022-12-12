FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
#RUN mvn clean package
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
