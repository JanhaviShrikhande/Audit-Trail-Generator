# build stage
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# copy Maven files
COPY pom.xml mvnw* ./
COPY .mvn .mvn

# give execute permission to mvnw (fix for Windows)
RUN chmod +x mvnw

# copy backend source
COPY src ./src

# build jar
RUN ./mvnw -B -DskipTests package

# runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# run Spring Boot on Render's provided PORT
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]

