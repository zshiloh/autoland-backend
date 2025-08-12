# Imagen base con JDK 21
FROM openjdk:21-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el proyecto completo
COPY . .

# Compilar el proyecto usando Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Exponer el puerto dinámico que Render asigna
EXPOSE 8081

# Comando para ejecutar la app
CMD ["java", "-jar", "target/ren-0.0.1-SNAPSHOT.jar"]