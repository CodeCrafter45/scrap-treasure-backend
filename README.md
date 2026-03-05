# ♻️ Scrap Treasure – Scrap Collection Management System

##  Overview

**Scrap Treasure** is a web-based scrap collection management system inspired by ride-booking platforms like Uber/Ola.
It connects **clients who want to dispose of scrap** with **scrap collectors** who collect it and calculate payment based on weight and scrap category.

The system supports three roles:

* **Client** – creates scrap pickup requests
* **Collector** – accepts requests and collects scrap
* **Admin** – verifies collectors and manages scrap pricing

The platform ensures secure access using **JWT authentication** and role-based authorization.

---

#  System Workflow

1. Client registers and logs in.
2. Client creates a scrap pickup request.
3. Collector views available requests.
4. Collector accepts a request.
5. Collector collects scrap and enters weight.
6. System automatically calculates price based on scrap category.
7. Client can track request status.

Request lifecycle:

```
REQUESTED → ACCEPTED → COLLECTED → COMPLETED
```

---

# 👥 User Roles

## Client

* Register and login
* Create scrap pickup request
* Track request status
* View collected scrap details

## Collector

* Register and login
* View available scrap requests
* Accept a pickup request
* Enter scrap weight
* System calculates price automatically

## Admin

* Login to admin panel
* Verify collectors
* Create and manage scrap categories
* Set price per kg
* Control system operations

---

# 🏗️ System Architecture

```
Client / Collector / Admin
          │
          ▼
      REST API
      (Spring Boot)
          │
          ▼
     Service Layer
    Business Logic
          │
          ▼
     Repository Layer
     (Spring Data JPA)
          │
          ▼
        MySQL
```

The system follows a **layered architecture**:

```
Controller → Service → Repository → Database
```

---

# 🛠️ Tech Stack

Backend:

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate

Database:

* MySQL

Build Tool:

* Maven

API Testing:

* Postman

Frontend:

* HTML
* CSS
* JavaScript

---

# 📂 Project Structure

```
src/main/java/com/scraptreasure

config
 └── security

controller
 ├── auth
 ├── client
 ├── collector
 └── admin

service
 ├── auth
 ├── client
 ├── collector
 └── admin

repository

entity

dto

exception

util
```

---

# 🔐 Security

The system uses **JWT-based authentication**.

Steps:

1. User logs in
2. Server generates JWT token
3. Token is sent in request header

Example:

```
Authorization: Bearer <token>
```

Role-based access is enforced using:

```
CLIENT
COLLECTOR
ADMIN
```

---

# 📊 Database Tables

Main tables:

* users
* scrap_request
* scrap_collection_details
* scrap_category

---

# 📡 Important APIs

## Authentication

```
POST /api/auth/register
POST /api/auth/login
```

## Client APIs

```
POST /api/client/requests
GET /api/client/requests
```

## Collector APIs

```
GET /api/collector/requests
POST /api/collector/requests/{id}/accept
POST /api/collector/pickup/{id}
```

## Admin APIs

```
GET /api/admin/collectors
POST /api/admin/collectors/{id}/verify

POST /api/admin/scrap-categories
GET /api/admin/scrap-categories
POST /api/admin/scrap-categories/{id}/toggle
```

---

# ⚙️ Setup Instructions

1. Clone the repository

```
git clone <repo-link>
```

2. Configure MySQL database

Update `application.properties`

```
spring.datasource.url=jdbc:mysql://localhost:3306/scrap_treasure
spring.datasource.username=root
spring.datasource.password=yourpassword
```

3. Run the application

```
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

---

# 🧪 API Testing

Use **Postman** to test APIs.

Steps:

1. Register a user
2. Login and obtain JWT token
3. Use token in Authorization header
4. Test APIs

---

# 🚀 Future Improvements

* Location-based collector matching
* Payment gateway integration
* Real-time notifications
* Mobile application
* Admin analytics dashboard

---


