# Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Puerto que Render usará (usa variable PORT)
ENV PORT=8080
EXPOSE ${PORT}

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
