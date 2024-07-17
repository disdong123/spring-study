#!/bin/bash

kill_process_on_port() {
    local port="$1"
    local process_id=$(lsof -t -i :$port)

    if [ -n "$process_id" ]; then
        echo "Killing process $process_id using port $port"
        kill -9 $process_id
        echo "Process $process_id killed"
    else
        echo "No process is using port $port"
    fi
}

kill_process_on_port 8080
kill_process_on_port 8081
kill_process_on_port 8082
kill_process_on_port 8083

#docker build -t throughput-test .
#
#docker stop throughput-test-container > /dev/null 2>&1
#
#docker rm throughput-test-container > /dev/null 2>&1
#
#docker run -p 3316:3306 --name throughput-test-container throughput-test

cd ../../
pwd

#./gradlew clean :spring-reactive:throughput-test:mvc-server:build
#
#./gradlew :spring-reactive:throughput-test:mvc-server:bootRunApp1 &
#
#echo "mvc 1 start..."
#
#./gradlew :spring-reactive:throughput-test:mvc-server:bootRunApp2 &
#
#echo "mvc 2 start..."

./gradlew clean :spring-reactive:throughput-test:webflux-server:build

./gradlew :spring-reactive:throughput-test:webflux-server:bootRunApp1 &

echo "webflux 1 start..."

./gradlew :spring-reactive:throughput-test:webflux-server:bootRunApp2 &

echo "webflux 2 start..."