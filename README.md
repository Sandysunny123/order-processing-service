A Spring Boot microservice designed to manage orders, update statuses, and run scheduled tasks.
Built with secure Basic Authentication, proper layered architecture, exception handling, and Swagger API documentation.


Features

. Spring Security – Basic Authentication
. Order creation, update, cancel APIs
.Scheduled job for auto-updating order statuses
.Swagger UI for API documentation
.DTO + Entity + Mapper clean architecture
.Unit tests with JUnit + Mockito
.JPA + H2/MySQL configurable
.Centralized exception handling


TechStacks
| Layer      | Technology                   |
| ---------- | ---------------------------- |
| Language   | Java 21                      |
| Framework  | Spring Boot                  |
| Auth       | Spring Security (Basic Auth) |
| Database   | H2 / MySQL                   |
| API Docs   | Swagger (springdoc-openapi)  |
| Build Tool | Maven                        |
| Testing    | JUnit 5, Mockito             |




Authorization: Basic <base64(username:password)>


Swagger Documentation

Once the application is running, open:

Swagger UI

http://localhost:8080/swagger-ui/index.html


OpenAPI JSON

http://localhost:8080/v3/api-docs


How to Run the Project
1️⃣ Clone the Repository
git clone https://github.com/Sandysunny123/order-processing-service.git
cd order-processing-service

2️⃣ Configure the Application

Copy application-example.properties → application.properties:

src/main/resources/application-example.properties


Add your DB config + Basic Auth credentials.

3️⃣ Run the Application
mvn spring-boot:run


Or run:

OrderProcessingServiceApplication.java

🔗 API Endpoints
➕ Create Order
POST /orders

🔄 Update Order Status
PATCH /orders/{id}/status?value=PROCESSING

❌ Cancel Order
POST /orders/{id}/cancel

🧪 Running Tests
mvn test

