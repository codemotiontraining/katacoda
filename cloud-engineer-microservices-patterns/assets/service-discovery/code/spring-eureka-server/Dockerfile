FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/spring-eureka-server-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8761 
CMD ["java", "-jar", "/app/spring-eureka-server-1.0.0.jar"]  
