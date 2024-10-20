docker-compose -f docker-compose-oum-apis.yml pull
docker-compose -f docker-compose-oum-apis.yml down
docker-compose -f docker-compose-oum-apis.yml up -d
docker images -a | grep none | awk '{print $3}' | xargs -i docker rmi {}
docker update --restart unless-stopped $(docker ps -q)