curl -i -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"id":"ddd","nick":"sd@asd4.com", "avatar_id":1}' http://127.0.0.1:8080/create_user
curl -i -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"id":"ccc","nick":"sd@asd4.com", "avatar_id":1}' http://127.0.0.1:8080/create_user
curl -i -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"id":"ccc","nick":"sd@asd4.com", "avatar_id":"none"}' http://127.0.0.1:8080/create_user

