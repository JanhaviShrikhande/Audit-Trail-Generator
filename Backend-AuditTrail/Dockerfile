# build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# copy only maven files first for layer cache
COPY pom.xml mvnw ./
COPY .mvn .mvn
# copy backend source
COPY src ./src

# build jar (produces target/*.jar)
RUN ./mvnw -B -DskipTests package

# runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy jar from build stage (wildcard to avoid hardcoding artifactId)
COPY --from=build /app/target/*.jar app.jar

# Let Spring use Render's provided port environment variable
ENV JAVA_OPTS=""

# run the jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
