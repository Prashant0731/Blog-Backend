FROM maven:3.8.6-openjdk-8

RUN mkdir -p /root/.m2 \
    && mkdir /root/.m2/repository

WORKDIR /usr/src/app
ADD . .
RUN mvn clean package \
    -Dmaven.repo.local=/root/.m2/repository \
    -Dfindbugs.skip=true \
    -Dmaven.test.skip=true \
    -Dcheckstyle.skip=true \
    -Denforcer.skip=true
    
RUN mkdir /usr/src/build
RUN cp target/*.jar /usr/src/build/
RUN ls -la /usr/src/build/

FROM openjdk:8-jre-slim-stretch
RUN \
    set -e \
    && apt-get update \
    && apt-get install -y --no-install-recommends \
    build-essential git uuid-runtime perl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/src/app
COPY --from=0 /usr/src/build/*.jar /usr/src/app/
EXPOSE 8080

ENTRYPOINT ["java", "-jar","/Blog-Backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]

