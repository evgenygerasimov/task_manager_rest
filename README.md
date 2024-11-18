# Task Manager REST API

This is a simple Task Manager REST API built using Spring Boot. The application allows users to manage their tasks with features such as creating, updating, deleting, and fetching tasks. It is fully designed as a RESTful service.

## Features

- Create, read, update, and delete tasks
- User authentication (if implemented)
- Easy integration with PostgreSQL for data persistence
- Docker containerization for easy deployment

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose

### Clone the Repository

### Build the Application

To build the Spring Boot application, you can use Maven. Run the following command: mvnw clean package


### Running the Application with Docker

To run the application in Docker, you can use the provided `docker-compose.yml` file. This file also sets up a PostgreSQL database container.

1. Ensure Docker and Docker Compose are installed on your machine.
2. Navigate to the project directory.
3. Run the following command to start the application and the database: docker-compose up



