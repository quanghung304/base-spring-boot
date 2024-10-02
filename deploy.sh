VERSION_CORE=$1

PREVIOUS_CONTAINER=$(docker ps --format "table {{.ID}}  {{.Names}}  {{.CreatedAt}}" | grep cim-be | awk -F  "  " '{print $1}')
docker kill -s SIGTERM $PREVIOUS_CONTAINER

VERSION_CORE=${VERSION_CORE} docker-compose up -d
