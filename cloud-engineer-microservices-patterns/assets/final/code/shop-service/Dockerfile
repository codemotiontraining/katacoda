FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/shop-service-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8081 
CMD ["java", "-jar", "/app/shop-service-1.0.0.jar"]