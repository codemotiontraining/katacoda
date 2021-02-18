FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/discovery-shop-service-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8080 
CMD ["java", "-jar", "/app/discovery-shop-service-1.0.0.jar"]  
