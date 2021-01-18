#!/bin/bash
set -e

# Options:
#  nothing - runs all existing containers
#  build - build and run all containers
#  nameOfSomeServer - build and rerun that server, also runs the others if they are not running
#  stop - stops all the running containers

if [ $# -eq 0 ]
  then
    docker-compose up -d
else
    if [ "$1" = "tokenserver" ]
      then
        pushd tokenService
        mvnw package
        popd
        docker-compose up -d --force-recreate --build "$1"
    elif [ "$1" = "paymentserver" ]
      then
        pushd paymentService
        mvnw package
        popd
        docker-compose up -d --force-recreate --build "$1"
    elif [ "$1" = "accountserver" ]
      then
        pushd accountService
        mvnw package
        popd
        docker-compose up -d --force-recreate --build "$1"
    elif [ "$1" = "stop" ]
      then
        docker-compose down
    elif [ "$1" = "build" ]
      then
        pushd messaging-utilities
        mvnw clean install

        popd

        pushd paymentService
        mvnw package

        popd

        pushd tokenService
        mvnw package

        popd

        pushd accountService
        mvnw package

        popd

        docker-compose up --build -d
    fi
fi