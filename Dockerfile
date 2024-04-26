FROM openjdk:21-slim AS build
COPY . /code/jhipster-app/
WORKDIR /code/jhipster-app/
RUN --mount=type=cache,target=/root/.m2 chmod +x mvnw && ./mvnw package -B -DskipTests -Dmaven.javadoc.skip=true -Dmaven.source.skip
RUN mv /code/jhipster-app/target/*-exec.jar /code/

FROM openjdk:21-slim
COPY --from=build /code/*.jar /code/
RUN \
    # configure the "jhipster" user
    groupadd jhipster && \
    useradd jhipster -s /bin/bash -m -g jhipster -G sudo && \
    echo 'jhipster:jhipster'|chpasswd
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS=""
USER jhipster
CMD java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /code/*.jar
EXPOSE 7471
