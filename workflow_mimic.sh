docker-compose build frostini # build frostini docker
docker-compose run -d frostini
docker-compose build frostini-web # build frostini-web docker with the correct ip for the frostiniSpringBootDocker
docker-compose run -p 3000:80 -d frostini-web # run frostini-web and map port 80 to host's port 3000
curl localhost:3000
