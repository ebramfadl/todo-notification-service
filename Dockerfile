FROM openjdk:25-ea-4-jdk-oraclelinux9
WORKDIR /app
LABEL authors = "EBRAM FADL"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]