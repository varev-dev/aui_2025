FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV SERVER_PORT=8080
ENV CLUSTER_SERVICE_URL=http://cluster-service:8081
ENV NODE_SERVICE_URL=http://node-service:8082

ENTRYPOINT ["java", "-jar", "app.jar"]
