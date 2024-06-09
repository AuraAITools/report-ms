FROM openjdk:21-jdk
EXPOSE 8080
ADD report-api/target/report-api-0.0.1-SNAPSHOT.jar report-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT [ "java", "-jar","/report-api-0.0.1-SNAPSHOT.jar" ]