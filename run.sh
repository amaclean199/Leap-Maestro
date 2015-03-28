#!/bin/bash

mvn install:install-file -Dfile=src/main/resources/jar/LeapJava.jar -DgroupId=com.leapmotion.leap -DartifactId=leapMotion -Dversion=2.2.4 -Dpackaging=jar
mvn clean
mvn compile
mvn package
java -jar leap.hackwestern-1.0-SNAPSHOT-jar-with-dependencies.jar
