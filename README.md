# Simple Search Engine

This is a Simple Search Engine. The service works with documents, that contain tokens (words).
You can save, read and search documents.

### Starting services locally without Docker

Every microservice is a Spring Boot application and can be started locally using IDE or mvn spring-boot:run command.
Please note that both services `Server` and `Client` must be started. 
If everything goes well, you can access the following services at given location:

* Search Engine Server - http://localhost:8001 
* Search Engine Client - http://localhost:8000

Although, you should use the service via `Client`.

### Starting services locally with docker-compose

In order to start entire infrastructure using Docker, you have to build the project by executing
`mvn clean install`.
Once build is ready, you can start the services with a single command `docker-compose up`

### API 

* `POST /documents` - save a document
* `GET  /documents/{key}` - get a document by key
* `GET  /documents/search?{token}` - search documents keys by tokens 


### Usage

```
1. 
curl http://localhost:8000/documents \
-H "Content-type: application/json" \
-d '{"key": "1", "data": "blue green"}'

2. 
curl http://localhost:8000/documents/1

3. 
curl http://localhost:8000/documents \
-H "Content-type: application/json" \
-d '{"key": "2", "data": "blue black"}'

4. 
curl http://localhost:8000/documents \
-H "Content-type: application/json" \
-d '{"key": "3", "data": "white red"}'

5.
curl http://localhost:8000/documents/search?token=blue 
curl "http://localhost:8000/documents/search?token=blue&token=red" 

```