docker rmi comment-service:latest
docker rmi vebstechbee03/tech-bee:comment-service-latest
docker build -t comment-service:latest .
docker tag comment-service:latest vebstechbee03/tech-bee:comment-service-latest
docker push vebstechbee03/tech-bee:comment-service-latest