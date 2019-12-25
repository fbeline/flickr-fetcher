FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/flickr-fetcher-0.0.1-SNAPSHOT-standalone.jar /flickr-fetcher/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/flickr-fetcher/app.jar"]
