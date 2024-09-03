Clone this and two other microservices and place them in one folder, also docker-compose.yml and init.sql from "Docker" git repository

The stucture has to be like this:
MicroserviceApplication{
  |AuthService|
  |BookService|
  |LibraryService|
  |docker-compose.yml|
  |init.sql|
}

The first file "docker-compose.yml" contains configuration for docker containers, Postgre, connectivity
The second file contains an sql script that is used to create a container with a PostgreSQL database container used in the Library microservice application

Instruction for starting the app:
1) Clone repositories with microsevices and place them in one folder
2) Clone repository with docker-compose.yml and init.sql and place it on the same level as the services (in the same folder)
3) Do mvn clean package for every single microservice using terminal or inteligi idea interface features to create a jar file that is used for docker (it will be created where needed and named as needed)
5) Build the docker images, start the services defined in the docker-compose.yml file using inteligi idea or terminal

P.S. while running mvn clean package tests will be passing, to make it build and run faster u can do packaging with no testing if needed

After running all the microservices go to 
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
http://localhost:8083/swagger-ui/index.html
for using and testing the app comfortably and with the necessary instructions
