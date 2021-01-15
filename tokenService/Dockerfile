FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/lib /usr/src/lib
COPY target/tokenservice-1.0-SNAPSHOT-runner.jar /usr/src
CMD java -Xmx64m \-jar tokenservice-1.0-SNAPSHOT-runner.jar
