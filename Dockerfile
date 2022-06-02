FROM openjdk:17-oracle
ADD /target/clubtab-1.0-SNAPSHOT.jar clubtab.jar
ENTRYPOINT ["java", "-jar", "clubtab.jar"]
