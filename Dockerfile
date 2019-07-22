FROM openjdk:11-jdk
MAINTAINER Varun Shrivastava <varunshrivastava007@gmail.com>

EXPOSE 8080

ADD target/counterclinic-0.0.1-SNAPSHOT.jar counterclinic-walkin.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "counterclinic-walkin.jar"]
