# CEBP

# Project Overview

Our project is a Java-based bureaucratic system designed to streamline the process of handling document issuance in an office environment. The system leverages the Java Concurrency API to efficiently manage a queue of document requests across multiple offices and counters. It is built with flexibility in mind, allowing it to handle an arbitrary number of offices, each with an arbitrary number of counters, ensuring scalability and adaptability.

# How it works

Document Queue: Each office maintains its own queue of document requests, which are processed at multiple counters simultaneously.
Concurrency Control: The system makes use of Java's concurrency tools, such as ExecutorService, Locks, Semaphores, and BlockingQueues, to manage the parallel processing of tasks across multiple offices and counters.
Scalability: The number of offices and counters can be adjusted dynamically without major modifications to the underlying architecture.

Concurrency is managed by using a FIFO queue based on time to order the issues.

# Tehnologies 

- Java
- C# for a standalone application
- React, HTML, CSS, JavaScript for a web based application

# Schema of the project

![image](https://github.com/user-attachments/assets/f6c598d8-ebce-4929-87b9-dbd9cb53c5e3)


![image](https://github.com/user-attachments/assets/db860e30-4546-48ac-a7f2-34d1a4aa4d7b)
