FROM maven:3-adoptopenjdk-11 as MAVEN_BUILD

RUN mkdir /build

COPY pom.xml /build
COPY src /build/src

WORKDIR /build
RUN mvn clean package -DskipTests=true

FROM openjdk:11

WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/*.jar /app/app.jar

RUN mkdir /app/data
ENV H2_FILE_PATH=./data
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]
