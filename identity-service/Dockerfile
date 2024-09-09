# maven and jdk-21
FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

#build
RUN mvn package -DskipTests

#jdk-21
FROM amazoncorretto:21.0.4

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# command run application
ENTRYPOINT ["java", "-jar", "app.jar"]