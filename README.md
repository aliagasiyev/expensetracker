# 💸 Expense Tracker

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1.2-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.x-blue)
![Gradle](https://img.shields.io/badge/Build-Gradle-23D18B)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-orange)
![Swagger](https://img.shields.io/badge/API-SwaggerUI-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 📌 Project Overview

**Expense Tracker** is a backend system built with Java & Spring Boot to manage personal income and expenses. It allows users to create, update, view, delete, and analyze financial records.

### ✅ Key Features

- Clean RESTful API structure with DTO, Service, Controller layers
- Full CRUD support for expenses
- Search by date range
- Income/expense statistics
- Bean validation with helpful error messages
- Swagger UI integrated for API testing
- Global exception handling

---

## ⚙️ Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Validation
- Lombok
- Swagger / Springdoc OpenAPI
- PostgreSQL (via Spring Data JPA)
- Gradle (build tool)

---

## 📁 Project Structure

```
src/main/java/com/turing/expensetracker
├── controller        # REST Controllers
├── dto               # Request and Response DTOs
├── entity            # Expense entity
├── exception         # Global error handling
├── mapper            # DTO ↔ Entity mappers (optional for future)
├── repository        # Spring Data JPA Repositories
├── service           # Business logic
```

---

## 📡 API Endpoints

All endpoints are prefixed with `/api/v1/expenses`

| Method | Endpoint                            | Description                        |
|--------|-------------------------------------|------------------------------------|
| POST   | `/`                                 | Create a new expense               |
| GET    | `/`                                 | Get all expenses                   |
| GET    | `/{id}`                             | Get a specific expense by ID       |
| PUT    | `/{id}`                             | Update an expense                  |
| DELETE | `/{id}`                             | Delete an expense by ID            |
| GET    | `/range?from=2025-05-01&to=2025-05-07` | Get expenses in date range     |
| GET    | `/statistics`                       | Get total income and total expense |

---

## 🧪 Sample Create Request

```json
POST /api/v1/expenses
Content-Type: application/json

{
  "title": "May Salary",
  "amount": 1200.50,
  "category": "Income",
  "date": "2025-05-01",
  "description": "Salary for May",
  "income": true
}
```

---

## 📷 Swagger UI

---

## 🧭 Swagger UI Preview

The project provides a complete interactive API documentation via Swagger:

![Swagger Screenshot](docs/swagger-ui.png)

You can access it at:

```
http://localhost:8081/swagger-ui/index.html
```

---

> ℹ️ To update this screenshot, take a new one and replace the image in `/docs/swagger-ui.png` inside your repo.


Visit:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 🚀 Future Enhancements

- 🔐 User Authentication (JWT)
- 📊 Category breakdown & charts
- 📅 Monthly reports
- 📎 Export to CSV/Excel
- 🌍 Multi-user support

