FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/discovery-product-service-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8081
CMD ["java", "-jar", "/app/discovery-product-service-1.0.0.jar"]