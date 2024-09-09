### build application ignore test

`mvn package -DskipTest`

### run application in docker use mysql local

`docker run --name identity-service -p 8081:8081 -e DBMS_CONNECTION=jdbc:mysql://host.docker.internal:3306/parking_identity -e DBMS_PASSWORD= -e DBMS_USERNAME=root -e KAFKA_SERVER=localhost:9094 -e JWT_SIGNER_KEY=6FX76JH4FjpvYli6NCHHsE+lie8jU6/DA14Y0ljNKifE8zGXMX2J/cu7nPGta1kq -e PROFILE_URL=http://localhost:8083/profile -e VAULT_URL=http://localhost:8084/vault  dangchien2601/identity-service:0.0.1`

###