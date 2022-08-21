# timao-e-pumba


Como rodar o repo?

Com Dockerfile:
Primeiro subir um container mysql local com o comando:
docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=jabbaTheHut2 -e MYSQL_DATABASE=timao_e_pumba -e MYSQL_USER=timao_e_pumba -e MYSQL_PASSWORD=jabbaTheHut2 mysql
