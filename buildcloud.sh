#!/bin/sh

# build java service image
mvn clean package -DskipTests
ossutil cp ./target/core-0.0.1-SNAPSHOT.jar oss://diarycore/deploy/app.jar -f
