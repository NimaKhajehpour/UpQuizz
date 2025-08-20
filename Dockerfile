# ---- Build stage ----
FROM gradle:8.9-jdk21 AS build
WORKDIR /app

# Copy Gradle files first for caching
COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || return 0

# Copy project source
COPY . .

# Build Spring Boot fat JAR
RUN gradle bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Run app
ENTRYPOINT ["java","-jar","/app/app.jar"]