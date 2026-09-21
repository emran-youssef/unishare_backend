# ---- Build stage ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy wrapper + pom first so deps are cached separately from source changes
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Now copy the rest and build
COPY src ./srcprod
RUN ./mvnw clean package -DskipTests -B

# ---- Run stage ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Railway = limited RAM, so cap the JVM heap
ENV JAVA_OPTS="-Xmx400m -Xss512k -XX:MaxMetaspaceSize=200m"

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]