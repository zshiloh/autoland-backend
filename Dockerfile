# Imagen base con JDK 21
FROM openjdk:21-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo lo necesario primero
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Compilar sin ejecutar tests
RUN ./mvnw clean package -DskipTests

# Puerto para Render
ENV PORT=8080
EXPOSE ${PORT}

# Ejecutar el .jar (ajusta el nombre si es diferente)
CMD ["java", "-Dserver.port=${PORT}", "-jar", "target/autoland-0.0.1-SNAPSHOT.jar"]
