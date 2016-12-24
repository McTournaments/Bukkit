#!/usr/bin/env bash
mkdir target
mkdir target/spigot
cd target/spigot

wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
export MAVEN_OPTS="-Xmx2048m"
java -jar BuildTools.jar --rev 1.11.2

cd ..

mkdir modularity
cd modularity

wget https://github.com/Desetude/Modularity/releases/download/1.0-SNAPSHOT/Modularity-1.0-SNAPSHOT.jar

cd ../..

mvn install:install-file -Dfile=target/spigot/spigot-1.11.2.jar -DgroupId=org.spigotmc -DartifactId=spigot -Dpackaging=jar -Dversion=1.11.2-R0.1-SNAPSHOT
mvn install:install-file -Dfile=target/modularity/Modularity-1.0-SNAPSHOT.jar -DgroupId=com.harryfreeborough -DartifactId=modularity -Dpackaging=jar -Dversion=1.0-SNAPSHOT
