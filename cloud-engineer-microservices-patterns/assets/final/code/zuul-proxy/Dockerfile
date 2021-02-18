FROM openjdk:8-jdk-alpine
RUN mkdir /app 
ADD target/zuul-proxy-1.0.0.jar /app/
WORKDIR /app
EXPOSE 8080 
CMD ["java", "-jar", "/app/zuul-proxy-1.0.0.jar"]