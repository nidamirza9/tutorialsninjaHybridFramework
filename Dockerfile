FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /workspace

# Pre-cache dependencies when pom changes
COPY pom.xml .
COPY resources ./resources
COPY src ./src

RUN mvn -B -q -DskipTests dependency:go-offline || true

CMD ["mvn", "-B", "-Pci", "test"]
