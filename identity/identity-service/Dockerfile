FROM public.ecr.aws/amazoncorretto/amazoncorretto:21
WORKDIR /app
COPY ./build/libs/identity-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8083