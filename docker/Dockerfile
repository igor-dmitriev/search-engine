FROM java:8
MAINTAINER Igor Dmitriev
ARG jarFile
ADD ${jarFile} /app.jar
RUN ls -la /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE ${EXPOSED_PORT}
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]