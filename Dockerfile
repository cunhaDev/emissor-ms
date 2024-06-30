FROM openjdk:11 as build
WORKDIR /tmp
COPY . .
RUN ./gradlew build -x test -x check --stacktrace

FROM openjdk:11
ENV ARTIFACT_NAME=emissor-ms.jar
WORKDIR /emissor-ms/
COPY --from=build /tmp/build/libs/$ARTIFACT_NAME .
ENTRYPOINT ["java", "-Xms300m", "-Xmx1G", "-XX:+PrintFlagsFinal", "-XX:MinRAMPercentage=50.0", "-XX:MaxRAMPercentage=90.0", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=GMT-3", "-XX:+UseContainerSupport", "-jar", "emissor-ms.jar"]
