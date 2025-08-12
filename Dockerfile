# Imagen base con JDK 17
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo lo necesario primero
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Compilar sin ejecutar tests
RUN ./mvnw clean package -DskipTests

# Puerto para Render
ENV PORT=8080
EXPOSE ${PORT}

# Ejecutar el .jar (ajusta el nombre si es diferente)
CMD ["java", "-Dserver.port=${PORT}", "-jar", "target/autoland-0.0.1-SNAPSHOT.jar"]
