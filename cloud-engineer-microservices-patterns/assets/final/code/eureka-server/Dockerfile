FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/eureka-server-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8761 
CMD ["java", "-jar", "/app/eureka-server-1.0.0.jar"]
