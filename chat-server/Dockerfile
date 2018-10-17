FROM clojure:lein-2.8.1-alpine as build-stage

# Clojure app

WORKDIR /usr/src/app

COPY project.clj /usr/src/app/

RUN lein deps

COPY . /usr/src/app

RUN lein uberjar

# ----------

FROM openjdk:8-alpine

# Java

COPY --from=build-stage /usr/src/app/target/chat-server.jar .

EXPOSE 8080

CMD ["java", "-jar", "chat-server.jar"]
