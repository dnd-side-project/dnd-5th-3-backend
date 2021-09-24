FROM openjdk:11-jdk

WORKDIR /home/myapp
COPY dnd-5th-3-backend-0.0.1-SNAPSHOT.jar /home/myapp/app.jar
EXPOSE 8080
VOLUME ["/home/myapp/log"]
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "/home/myapp/app.jar"]