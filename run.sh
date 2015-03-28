#!/bin/bash

mvn clean
mvn compile
mvn package
java -Djava.library.path=".:./lib/" -jar target/leap.hackwestern-1.0-SNAPSHOT-jar-with-dependencies.jar
