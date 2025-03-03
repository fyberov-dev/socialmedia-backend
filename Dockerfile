ARG HOME=/app

FROM gradle:8.10.2-jdk21 AS build

ARG HOME
ENV APP_HOME=$HOME
WORKDIR $HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

RUN gradle clean build

FROM openjdk:21-jdk-slim

ARG HOME
ENV APP_HOME=$HOME

COPY --from=build $APP_HOME/build/libs/iti0302-project-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java"]
CMD ["-Dspring.config.location=classpath:/application.yaml,file:/app/application.yaml", "-jar", "app.jar"]
