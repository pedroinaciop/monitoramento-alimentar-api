# Builder simplificado
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia tudo
COPY . .

# Build direto sem filtering complexo
RUN mvn clean package -DskipTests \
    -Dmaven.test.skip=true \
    -Dmaven.resources.encoding=UTF-8 \
    -Dfile.encoding=UTF-8

# Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]