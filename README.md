# jworker demo
Demo project for organizing background tasks scheduling based on Redis and jesque library

## Development environment requirements 

- jdk 1.8
- gradle
- Redis server 2.9 or later. Detailed information how to install and configure Redis you can find [here](http://redis.io/topics/quickstart)

## Build instructions

To build project use the following commands:

	gradle build

or

	./build.sh

## Run and test project

There are two scripts which can be used to run and test projects.

Use the following command to start the client:

	./runClient.sh

Use the following command to start the server:

	./runServer.sh

