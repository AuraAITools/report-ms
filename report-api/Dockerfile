FROM openjdk:21-jdk
EXPOSE 8080
ADD target/report-api-0.0.1-SNAPSHOT.jar report-api.jar
ENTRYPOINT [ "java", "-jar","/report-api.jar" ]
