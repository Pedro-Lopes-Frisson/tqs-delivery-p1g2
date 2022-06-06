docker-compose build frostini # build frostini docker
FROSTINI_ID=$(docker-compose run -d frostini) # start frostini docker
docker-compose build --build-arg API_HOSTNAME=$(docker inspect $FROSTINI_ID | grep IPAddress | awk -v RS='([0-9]+\\.){3}[0-9]+' 'RT{print RT}' | tail -n1) frostini-web # build frostini-web docker with the correct ip for the frostiniSpringBootDocker
docker-compose run -p 3000:80 -d frostini-web # run frostini-web and map port 80 to host's port 3000
curl localhost:3000
