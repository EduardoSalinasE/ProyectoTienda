FROM ibm-semeru-runtimes:open-11-jre-focal
EXPOSE 8080
ADD ./target/*.jar ./app.jar
CMD java -Djava.security-egd=file:/dev/./unrandon -jar app.jar