# BankService Project

## Overview
The BankService project is a Spring Boot application designed to provide essential banking services. It leverages various technologies to create a robust and secure banking system.

## Technologies Used
- **Spring Boot:** Main framework for building the application.
- **Spring Security:** Ensures secure authentication and authorization.
- **Spring Boot Test (H2 Database):** Enables testing with an in-memory H2 database.
- **Hibernate JPA:** Object-relational mapping for database interactions.
- **REST:** Architecture for building scalable web services.
- **Mapstruct (v1.5.3):** Simplifies the mapping between Java bean types.
- **Logging Aspect (v1.4.8):** Enhances logging capabilities for better debugging.
- **MySQL:** Relational database for storing banking data.
- **Liquibase:** Manages database schema changes over time.
- **JUnit 5:** Testing framework for unit and integration testing.
- **Swagger (v2.0.2):** API documentation for easy integration and testing.

## Setup and Configuration
1. Clone the repository: `git clone [repository-url]`
2. Configure MySQL database settings in `application.properties`.
3. Build the project using: `./mvnw clean install`
4. Run the application: `./mvnw spring-boot:run`

## API Documentation
Explore and test the API using Swagger:
- Local URL: `http://localhost:8080/swagger-ui.html`

## Testing
Run tests using JUnit 5:
```bash
./mvnw test
