# Cargo Delivery System

## Option №14

Cargo Delivery System.
The system provides access to services for receiving and sending packages.
The page of the calculator of cost and time necessary for performance of delivery with the set parameters is available to the not authenticated user.
The registered user can create cargo delivery in his cabinet, pay the bills created for him, confirm receipt of deliveries addressed to him and view payment statistics.
The administrator can view all users.
Delivery contains information about weight, destination, point of departure, and user of the recipient.
After creating a delivery request, the system creates an invoice that the user can pay.

## Вариант №14

Система Доставки Вантажів.  
Система надає доступ  до послуг по отриманню і відправці вантажів.
Не зарегістрованому користувачу доступна сторінка калькулятору вартості та часу, необхідних для виконання доставки з заданими параметрами.
Зарегістрований користувач у себе в кабінеті може створити запит на доставку вантажу, оплатити сформовані для нього рахунки, підтвердити отримання доставок адресованих йому та переглянути статистику платежів.
Адміністратор системи має змогу переглянути всіх користувачів. 
Доставка містить інформацію про вагу, пункт призначення, пункт відправки, та користувача отримувача.
Після створення запиту на доставку система формує рахунок, котрий користувач може оплатити.


# Installation and running

## Requirements

* JDK 1.8
* Spring Boot
* Apache Maven
* MySQL

## Running the project

Clone project to your local repository

Enter your MySQL username and password into properties spring.datasource.username and spring.datasource.password
in file /resources/db/application.properties

Enter url to your db into propertie spring.datasource.url in file /resources/db/application.properties

Run scripts from /resources/db/sql.sql folder to create database, tables and to insert data

From project root folder run - mvn spring-boot:run

Use http://localhost:8090/  to view website

# Default users

ADMIN adminSpring@ukr.net - admin

USER userSpring@ukr.net - user
