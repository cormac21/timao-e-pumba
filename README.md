# timao-e-pumba



### Com Dockerfile:
Primeiro subir um container mysql local com o comando:
```
docker run -p 3306:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=jabbaTheHut2 -e MYSQL_DATABASE=timao_e_pumba -e MYSQL_USER=timao_e_pumba -e MYSQL_PASSWORD=jabbaTheHut2 mysql
```


### Com docker compose:
Pra subir a aplicação
```
docker compose up
```

Pra descer a aplicação
```
docker compose down
```