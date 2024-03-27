#!/bin/sh

# build java service image
mvn clean package -DskipTests
docker build -t diarycore:latest .

# run everything
DATA_PATH=./data

if [ ! -d "$DATA_PATH/mysql" ]; then
    mkdir -p "$DATA_PATH/mysql"
    echo "$DATA_PATH/mysql 目录已创建"
else
    echo "$DATA_PATH/mysql 目录已存在"
fi

if [ ! -d "$DATA_PATH/es" ]; then
    mkdir -p "$DATA_PATH/es"
    echo "$DATA_PATH/es 目录已创建"
else
    echo "$DATA_PATH/es 目录已存在"
fi

DATA_PATH=$DATA_PATH docker-compose up

