### Commands

- sudo apt-get update

- sudo apt-get install mysql-server

- sudo mysql_secure_installation

- sudo mysql -u root

### database comands

- create database

`create database dummy;`

- switch database

`use dummy;`

- create table

`create table  orders ( \
  orderid int, \
  customername varchar(20));`

- insert info on the table

`insert into orders (orderid,customername) values (1,"Owen");`

- fetch or retrieve the data

`select * from orders;`

more documentation about [mysql](https://en.wikibooks.org/wiki/MySQL/CheatSheet)
