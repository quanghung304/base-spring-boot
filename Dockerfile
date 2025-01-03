#sondh
FROM eclipse-temurin:17-jdk-focal as build

WORKDIR /build

COPY .mvn/ ./.mvn
COPY mvnw pom.xml  ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY . .
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /build/target/*.jar run.jar
ENTRYPOINT ["java", "-jar", "/app/run.jar"]
