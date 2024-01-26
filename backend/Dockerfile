FROM docker.io/gradle:jdk21 as buildstage

COPY gradle/ /backend/gradle
COPY settings.gradle.kts module.yaml gradlew /backend/

WORKDIR /backend/

RUN gradle build

COPY src/ /backend/src/

RUN gradle clean installDist

FROM docker.io/eclipse-temurin:21

COPY --from=buildstage /backend/build/install/ /
WORKDIR /backend
RUN touch demo.conf

EXPOSE 7777

ENTRYPOINT ["/backend/bin/backend"]
