FROM maven:3.9.6-amazoncorretto-21 AS apicarrito-jar

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-alpine
LABEL authors="erik_antony"
COPY --from=apicarrito-jar /home/app/target/msvc-carrito-compras-0.0.1-SNAPSHOT.jar /usr/local/lib/carrito.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/carrito.jar"]