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

To Do:

1. ExpenseCategory CRUD (Category Management)

This feature allows users to categorize their expenses to keep better track of them. Users will be able to manage their own categories through this new ExpenseCategory model.

Endpoints:
POST /api/v1/categories - Create a new category.
GET /api/v1/categories - Get all categories.
GET /api/v1/categories/{id} - Get a specific category by its ID.
PUT /api/v1/categories/{id} - Update a category by its ID.
DELETE /api/v1/categories/{id} - Delete a category by its ID.
2. ExpenseSearch by Category

This feature enables users to search for expenses based on their categories. It will make it easier for users to analyze and manage their expenses quickly.

Endpoint:
GET /api/v1/expenses/category/{categoryId} - Get all expenses for a specific category.
3. UserProfile

Users will have the ability to view and update their profiles. This feature will allow users to manage their personal information associated with the application.

Endpoints:
GET /api/v1/users/{userId}/profile - Get the profile of a user by their ID.
PUT /api/v1/users/{userId}/profile - Update the profile of a user by their ID.
4. Expense Summary

This feature provides users with an overview of their expenses. It allows users to get summarized statistics for their expenses, such as monthly, yearly, or category-based summaries.

Endpoint:
GET /api/v1/expenses/summary - Get a summary of expenses (daily, monthly, yearly).
5. Expense Reminder

This feature allows users to set reminders for certain expenses, such as monthly recurring expenses. Users will be able to create, view, and delete expense reminders.

Endpoints:
POST /api/v1/expenses/reminder - Create an expense reminder.
GET /api/v1/expenses/reminders - Get all expense reminders.
DELETE /api/v1/expenses/reminder/{id} - Delete an expense reminder by its ID.



