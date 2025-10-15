# ![Icon](https://raw.githubusercontent.com/YuliiaNisha/images/18e6d5167add2c12de9a77719654e0fd68689464/bookstore-logo.png)
# Online Bookstore API

![Java](https://img.shields.io/badge/Java-17-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-green)
![Maven](https://img.shields.io/badge/Maven-3+-green)
![MySQL](https://img.shields.io/badge/Database-MySQL-green)
![JUnit](https://img.shields.io/badge/Tests-JUnit%205-green)
![Swagger](https://img.shields.io/badge/API-Swagger%20UI-green)

> Simple, efficient, and secure — the basic functionality of an online bookstore in one API.

---
## Table of Contents
- [Description](#description)
- [Technologies and Tools](#technologies-and-tools)
- [Controllers](#controllers)
- [Model Diagram](#model-diagram)
- [Postman](#postman)
- [How to use](#how-to-use)
- [Challenges and Solutions](#challenges-and-solutions)
- [API Documentation](#api-documentation)

## Description
Online Bookstore API is a RESTful application that provides the essential features of an online bookstore:
- Browsing and searching for books
- Managing categories
- Placing orders
- User registration and login

Its strength lies in its simplicity and efficiency: the project is lightweight, easy to understand, and at the same time offers the core functionality of a real-world e-commerce application.

---

## Technologies and Tools
This project employs:
- **Java 17** - The core programming language used to build the application.
- **Spring Boot 3.3.3** - Simplifies configuration, provides embedded server, and auto-configures dependencies.
- **Spring Data JPA 3.3.3** - Simplifies database access with repository abstraction.
- **Spring Validation 3.3.3** - Enables bean validation using annotations like @NotNull and @Email.
- **Spring Security 6.3.3** - Provides authentication, JWT token validation, and role-based authorisation.
- **MapStruct 1.6.2** - Efficient mapping between entities and DTOs.
- **Lombok 1.18.34** - Reduces boilerplate by auto-generating getters, setters, and constructors.
- **Liquibase 4.27.0** - Manages database schema changes and versioning in a structured way.
- **Springdoc OpenAPI (Swagger UI) 2.1.0** - Provides clear and user-friendly API documentation.
- **Testcontainers 1.21.3** - Enables running tests inside lightweight Docker containers for reproducible environments.
---

## Controllers
Controllers provide functionality to manage:
- Books
- Categories
- Orders
- Shopping cart

Additionally, the **AuthenticationController** handles user registration and login.

All endpoints can be divided into three groups:
1. **Publicly accessible** (AuthenticationController)
2. Accessible only to users with the **ADMIN role**
3. Accessible only to users with the **USER role**

| 🌐 PUBLIC Endpoints | 🔒 ADMIN Role Endpoints | 👤 USER Role Endpoints |
|---------------------|-------------------------|---------------------|
| Register         | Create Book          | Get Book By ID      |
| Login            | Update Book          | Get All Books       |
|                  | Delete Book          | Search for Books    |
|                  | Create Category      | Get All Categories  |
|                  | Update Category      | Get Category By ID  |
|                  | Delete Category      | Get Books By Category ID |
|                  | Update Order Status  | Create Order        |
|                  |                      | Get All Orders      |
|                  |                      | Get Order Items By Order ID |
|                  |                      | Get Order Item By ID |
|                  |                      | Get Shopping Cart |
|                  |                      | Add Book To Cart |
|                  |                      | Update Book Quantity in Cart |
|                  |                      | Delete Book From Cart |
---

## Model Diagram
The diagram below represents the entity relationships of the Bookstore:
![Model Diagram](https://raw.githubusercontent.com/YuliiaNisha/images/1a3b90504fe95c93d5cff937b05292a22142a8e7/bookstoreModelDiagram.png)
---

## Postman
A Postman collection is available to simplify testing the API.  
👉 [Open Online Bookstore Postman Collection](https://postman.co/workspace/My-Workspace~49ed7a22-2d52-45ef-8ca1-c68f46105379/collection/40367151-d5b4ff87-5102-4633-b53f-afb2a9a5b27e?action=share&creator=40367151)

How to use this Postman collection:
1. Click the link above.
2. Import the collection into your Postman app.
3. Run requests against http://localhost:8080/api/
4. Authenticate by registering and/or logging in to get a JWT token.
5. Go to the Authorization tab.
6. Choose Bearer Token as the Auth Type.
7. Paste the JWT token you received into the token field to access all protected endpoints.

![Token Flow Diagram](https://raw.githubusercontent.com/YuliiaNisha/images/main/token-flow.png)

For a quick overview of how to use the Postman collection and test the API endpoints, watch this video:  
👉 [Postman demo](https://www.loom.com/share/8afbfe7a809e4c9da0bd496bc78d1193?sid=2c4f6647-b0a5-4369-9837-2efef8414830)
---

## How to use
### Run Locally
1. Clone the repository
   ```bash
   git clone https://github.com/YuliiaNisha/online-bookstore.git
   cd online-bookstore
   ```
2. Configure the database in src/main/resources/application.properties
3. Configure Environment Variables
- Create a `.env` file in the project root.
- Copy `.env.sample` into your `.env` file.
- Fill in the required credentials (database URL, username, password, etc.).
4. Build the project
```bash
mvn clean package
```
5. Run the application
6. Open Swagger UI in your browser to test endpoints: http://localhost:8080/swagger-ui/index.html

### Use the Deployed API (AWS)

The API is deployed on AWS, making it accessible to anyone around the world. This means that any user can try out all API features without installing or configuring anything locally.

1. Open your browser and go to the deployed Swagger UI URL:
   http://ec2-16-171-2-102.eu-north-1.compute.amazonaws.com/api/swagger-ui/index.html
2. Use Swagger UI to explore all available endpoints, check request and response models, and test the API directly in the browser.
---

## Challenges and Solutions

### Restricting Access to Endpoints
#### Challenge:
* Certain endpoints needed to be accessible only to users with role ADMIN
#### Solution:
* Integrated Spring Security with JWT tokens and role-based access.

### Entity-to-DTO Mapping
#### Challenge:
* Default MapStruct mapping methods were insufficient for converting some entities to DTOs and back.
#### Solution:
* Implemented custom mapping methods.
---

## API Documentation
The Online Bookstore API includes interactive documentation powered by Swagger UI.
It provides a convenient way to:
* Explore all available endpoints
* Inspect request and response models
* Authenticate with JWT and test secured endpoints
* Execute API calls directly from the browser

![Swagger Demo](https://raw.githubusercontent.com/YuliiaNisha/images/f1d443f8fb3accb18775c451bf678a99b4d67b39/Swagger.gif)

### Access Swagger documentation
- Run the application.
- Open your browser and go to:
```bash
http://localhost:8080/api/swagger-ui/index.html
```
---
