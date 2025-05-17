FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
LABEL authors = "Ebram Fadl"
COPY target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
