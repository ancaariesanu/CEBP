# CEBP

# Project Overview

Our project is a Java-based bureaucratic system designed to streamline the process of handling document issuance in an office environment. The system leverages the Java Concurrency API to efficiently manage a queue of document requests across multiple offices and counters. It is built with flexibility in mind, allowing it to handle an arbitrary number of offices, each with an arbitrary number of counters, ensuring scalability and adaptability.

# How it works
Concurrency is managed by using a FIFO queue based on time to order the issues.


Document Queue: Each office maintains its own queue of document requests, which are processed at multiple counters simultaneously.

Concurrency Control: The system makes use of Java's concurrency tools, such as ExecutorService, Locks, Semaphores, and BlockingQueues, to manage the parallel processing of tasks across multiple offices and counters.

Scalability: The number of offices and counters can be adjusted dynamically without major modifications to the underlying architecture.

Side note: Any counter can be closed due to "important reasons" (Coffee breaks).


# Tehnologies 

- Java.
- C#, Windows Forms for a standalone application.
- React, HTML, CSS, JavaScript for a web based application.
- Swagger UI & Postman, for testing the endpoints.
- Amazon Web Services, to run a server with the backend logic and make the connections for the clients with the database.
- SQL for making queries in the Supabase database.
- Supabase for authentication and database.
# Schema of the project

![image](https://github.com/user-attachments/assets/f6c598d8-ebce-4929-87b9-dbd9cb53c5e3)


![image](https://github.com/user-attachments/assets/db860e30-4546-48ac-a7f2-34d1a4aa4d7b)

# Prerequisites
## For backend
### If you have Jetbrains Intellij installed:
You can import the project and let the IDE download all the maven plugins.

### If you don't have Jetbrains Intellij installed:
#### For linux:
Install maven and any Java 17 JDK.
For Ubuntu
```
sudo apt update
sudo apt install openjdk-17-jdk maven -y
```
After having all these packages enter inside the src folder and run ```mvn clean install```.

#### For Windows:
You can use  ```.\mvnw.exe``` to run maven comands.
Just like this: ```.\mvn.exe clean install```.

## For frontend
- ```npm``` installed on the computer
- any web browser
Once you have npm enter inside the UI_src folder and run ```npm install``` to download all the packages.


# How to run
## For backend
### If you have Intellij:
You can run the ```MyApplication.java``` file.

### If you have Windows or a Linux machine without Intellij:
For Linux
```mvn spring-boot:test-run```

For Windows
```.\mvnw.exe spring-boot:test-run```

#### After this you can enter on the Swagger page to test out the endpoints:
```localhost:8080/swagger-ui/index.html```

#### We also have a AWS server with the backend but it's turned off to save costs.
## For frontend
You need to run ```npm start``` on the root folder.

