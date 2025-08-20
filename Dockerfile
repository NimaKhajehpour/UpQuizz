# ---- Build stage ----
FROM gradle:8.9-jdk21 AS build
WORKDIR /app

# Copy Gradle wrapper and config
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle gradle.properties* ./

# Give permission to wrapper
RUN chmod +x ./gradlew

# Download dependencies (caching layer)
RUN ./gradlew dependencies --no-daemon || return 0

# Copy project source
COPY . .

# Build Spring Boot fat JAR
RUN ./gradlew bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Run Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]