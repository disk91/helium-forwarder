POSTGRESIP=$(shell docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' `docker ps | grep postgres | cut -d " " -f 1`) 
NETWORK=$(shell docker network ls | grep chirpstack | cut -d " " -f 1)
CONSOLE_DIR=/helium

.FORCE:

balancer: .FORCE
	./gradlew build -x test && docker build -t disk91/forwarder:1.0.0 .

nodes: .FORCE
	./gradlew build -x test && docker build -t disk91/forwarder:2.0.0 .

init: nodes balancer

build: nodes

update-nodes: nodes
	cd $(CONSOLE_DIR) ; docker compose stop fwnode1
	cd $(CONSOLE_DIR) ; docker compose up -d fwnode1
	sleep 10
	cd $(CONSOLE_DIR) ; docker compose stop fwnode2
	cd $(CONSOLE_DIR) ; docker compose up -d fwnode2

update-balancer: balancer
	cd $(CONSOLE_DIR) ; docker compose up -d fwdlb

