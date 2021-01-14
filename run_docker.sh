#!/bin/bash
set -e

pushd paymentService
mvn package 

popd

pushd tokenService
mvn package

popd

docker-compose up --build -d