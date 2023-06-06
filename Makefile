POSTGRESIP=$(shell docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' `docker ps | grep postgres | cut -d " " -f 1`) 
NETWORK=$(shell docker network ls | grep chirpstack | cut -d " " -f 1)
CONSOLE_DIR=/helium

.FORCE:

init: .FORCE
	./gradlew build -x test && docker build -t disk91/console:1.0.0 .

back: .FORCE
	./gradlew build -x test && docker build -t disk91/console:2.0.0 .

build: back



