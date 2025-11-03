FROM public.ecr.aws/docker/library/openjdk:17
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]