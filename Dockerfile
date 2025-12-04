FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Força encoding UTF-8 no Maven
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Build seguro sem filtering quebrar
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV TZ=America/Sao_Paulo
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Duser.timezone=America/Sao_Paulo"

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
