FROM amazoncorretto:18.0.2-alpine3.15
LABEL maintainer="gaio.lucas0@gmail.com"
WORKDIR /myapp
COPY build/libs/timao-e-pumba-0.0.1-SNAPSHOT.jar /myapp/timao-e-pumba.jar
ENTRYPOINT ["java", "-jar", "timao-e-pumba.jar"]