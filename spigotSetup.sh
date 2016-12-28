#!/usr/bin/env bash
mkdir target
mkdir target/spigot
cd target/spigot

wget https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar
java -jar BuildTools.jar --rev 1.11.2

cd ..

mkdir modularity
cd modularity

wget https://github.com/Desetude/Modularity/releases/download/1.0-SNAPSHOT/Modularity-1.0-SNAPSHOT.jar

cd ../..

mvn install:install-file -Dfile=target/modularity/Modularity-1.0-SNAPSHOT.jar -DgroupId=com.harryfreeborough -DartifactId=modularity -Dpackaging=jar -Dversion=1.0-SNAPSHOT
