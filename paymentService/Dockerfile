FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/lib /usr/src/lib
COPY target/paymentserver-1.0.0-SNAPSHOT-runner.jar /usr/src
CMD java -Xmx64m \-jar paymentserver-1.0.0-SNAPSHOT-runner.jar
