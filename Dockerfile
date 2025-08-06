FROM eclipse-temurin:21-jdk-alpine

# Crie um diretório de trabalho
WORKDIR /app

# Copie os arquivos do projeto
COPY . .

# Torne o mvnw executável
RUN chmod +x mvnw

# Compile e empacote o projeto
RUN ./mvnw clean package -DskipTests

# Execute o jar gerado
CMD ["java", "-jar", "target/financeiro-0.0.1-SNAPSHOT.jar"]
