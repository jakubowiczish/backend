#
# Build stage
#
FROM maven:3.6.3-jdk-14 AS build
COPY pom.xml /home/app/
WORKDIR /home/app
RUN mvn dependency:go-offline
COPY src /home/app/src
RUN mvn -f /home/app/pom.xml clean package dependency:resolve

#
# Package stage
#
FROM openjdk:14
COPY --from=build /home/app/target/master-diet-backend-application.jar /usr/local/lib/master-diet-backend-application.jar
EXPOSE 8081 8001
ENTRYPOINT ["java", "-Xmx4096m", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8001", "-noverify", "-jar","/usr/local/lib/master-diet-backend-application.jar"]