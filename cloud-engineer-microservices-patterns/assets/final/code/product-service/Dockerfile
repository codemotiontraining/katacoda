FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/product-service-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8085
CMD ["java", "-jar", "/app/product-service-1.0.0.jar"]