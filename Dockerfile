FROM adoptopenjdk:14-jre-hotspot

RUN mkdir /app

WORKDIR /app

ADD ./api/target/video-upload-api-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "video-upload-api-1.0-SNAPSHOT.jar"]
