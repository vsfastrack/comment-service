./gradlew clean build
docker build -t comment-service:0.0.1-RELEASE .
docker tag comment-service:0.0.1-RELEASE vebstechbee03/tech-bee:comment-service-0.0.1-RELEASE
docker push vebstechbee03/tech-bee:comment-service-0.0.1-RELEASE