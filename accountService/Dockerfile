FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/lib /usr/src/lib
COPY target/accountservice-1.0-SNAPSHOT-runner.jar /usr/src
CMD java -Xmx64m \-jar accountservice-1.0-SNAPSHOT-runner.jar
