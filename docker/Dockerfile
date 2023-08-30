#buildkit is activated by default on host docker
FROM yannoff/maven:3.8.5-openjdk-19-alpine AS builder
WORKDIR /opt/disi/
COPY pom.xml pom.xml
RUN mvn -e -B dependency:resolve
COPY src/ src/
RUN mvn clean package -DskipTests
 
FROM openjdk:19-alpine
WORKDIR /opt/disi/
COPY --from=builder /opt/disi/target/*.jar disi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "disi.jar"]
