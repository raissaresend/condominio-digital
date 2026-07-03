# Estágio de build (compilação)
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .
# Baixa as dependências (otimização de cache do Docker)
RUN ./mvnw dependency:go-offline -B
# Copia o código fonte e compila
COPY src src
RUN ./mvnw clean package -DskipTests

# Estágio de execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Copia a chave HTTPS (se você tiver uma na pasta raiz)
# COPY .keystore /root/.keystore
ENTRYPOINT ["java", "-jar", "app.jar"]
