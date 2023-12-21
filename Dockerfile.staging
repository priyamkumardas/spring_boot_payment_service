FROM maven:3.8.4-jdk-11-slim as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
ARG BUILD_NUMBER
ENV BUILD_NUMBER=${BUILD_NUMBER} 
COPY --from=build /home/app/target/sarvm-payment-service.jar /usr/local/lib/demo.jar
EXPOSE 8001
#ENTRYPOINT ["ls"]
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]