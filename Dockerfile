FROM amazoncorretto:18.0.2-alpine3.15
LABEL maintainer="gaio.lucas0@gmail.com"
WORKDIR /myapp
ADD build/libs/timao-e-pumba-0.0.1-SNAPSHOT.jar webapp.jar
ENTRYPOINT ["java", "-jar", "webapp.jar"]