# bill-share
App for share a bill

Версия Java - 17
Версия Postgres - 15

Для подключения к базе данных, в файле application.property поменяйте следующие значения: "bill_share_db" - название базы данных можете установить своё или оставить это spring.datasource.url=jdbc:postgresql://localhost:5432/bill_share_db

#Введите свой логин от базы данных spring.datasource.username=root

#Введите свой пароль от базы данных spring.datasource.password=root

Запустить приложение

Для запуска приложения в Docker - убедитесь в корректности пути к папке с приложением в файле docker-compose.yml и введите в терминале команду docker-compose up
![This is path to directory with app](https://github.com/Oyns/bill-share/blob/master/docker%20instruction.png)

Диаграмма связей базы данных.

![This is relations diagram](https://github.com/Oyns/bill-share/blob/master/bill%20share%20schema.jpg)
