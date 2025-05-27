![Gemini_Generated_Image_t5e7owt5e7owt5e7](https://github.com/user-attachments/assets/7f79361a-bd97-4f32-9109-bbea4610d7f9)# 💸 Expense Tracker

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1.2-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15.x-blue)
![Gradle](https://img.shields.io/badge/Build-Gradle-23D18B)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-orange)
![Swagger](https://img.shields.io/badge/API-SwaggerUI-yellow)
![License](https://img.shields.io/badge/License-MIT-lightgrey)


### 🧭 System Architecture

![System Architecture](![Uploading Gemini_Generated_Image_t5e7owt5e7owt5e7.png…]()
)



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

## 🚀 Future Enhancements

- 🔐 User Authentication (JWT)
- 📊 Category breakdown & charts
- 📅 Monthly reports
- 📎 Export to CSV/Excel
- 🌍 Multi-user support

## 🧩 Overall Architecture

- **Microservices**: User Service, Expense Service, Analytics Service, Notification Service  
- **API Gateway**: Spring Cloud Gateway  
- **Service Discovery**: Eureka  
- **Config Server**: Centralized configuration management  
- **Infrastructure**: PostgreSQL, Kafka, Docker  
- **Inter-service Communication**:
  - REST (documented via Swagger)
  - Kafka (for asynchronous triggers)

---

## 👤 1. User Service – Authentication & Profile Management

**Responsibilities:**
- Registration, login, profile management, password updates, role-based access (USER, ADMIN)

**Endpoints:**

| Method | Endpoint                     | Description                          |
|--------|------------------------------|--------------------------------------|
| POST   | `/auth/register`             | Register a new user                  |
| POST   | `/auth/login`                | Log in to the system                 |
| GET    | `/users/me`                  | Retrieve own profile via JWT token   |
| GET    | `/users/{id}`                | View another user’s profile          |
| PUT    | `/users/update-profile`      | Update own profile                   |
| PUT    | `/users/change-password`     | Change password                      |
| DELETE | `/users/{id}`                | Delete user (admin only)             |

**Technologies**: Spring Security, JWT, PostgreSQL

---

## 💸 2. Expense Service – Expense Management

**Responsibilities:**
- CRUD operations for expenses, category management, filtering and searching

**Endpoints:**

| Method | Endpoint                                | Description                           |
|--------|------------------------------------------|---------------------------------------|
| POST   | `/expenses`                              | Create a new expense                  |
| GET    | `/expenses/user/{userId}`                | Get all expenses for a user           |
| GET    | `/expenses/{id}`                         | Get a specific expense by ID          |
| PUT    | `/expenses/{id}`                         | Update an expense                     |
| DELETE | `/expenses/{id}`                         | Delete an expense                     |
| GET    | `/expenses/search?category=&date=&min=`  | Search and filter expenses            |
| GET    | `/expenses/categories`                   | Get all categories                    |
| POST   | `/expenses/categories`                   | Create a new category                 |
| PUT    | `/expenses/categories/{id}`              | Update a category                     |
| DELETE | `/expenses/categories/{id}`              | Delete a category                     |

---

## 📈 3. Analytics Service – Statistics & Graphs

**Responsibilities:**
- Monthly and overall expense reporting, chart-ready data output, comparisons

**Endpoints:**

| Method | Endpoint                                      | Description                                |
|--------|-----------------------------------------------|--------------------------------------------|
| GET    | `/analytics/monthly/{userId}?month=YYYY-MM`   | Monthly expense report                     |
| GET    | `/analytics/summary/{userId}`                 | Overall statistics                         |
| GET    | `/analytics/by-category/{userId}`             | Expenses grouped by category               |
| GET    | `/analytics/chart-data/{userId}`              | Data formatted for charting libraries      |
| GET    | `/analytics/compare-months/{userId}`          | Compare current vs previous month          |
| GET    | `/analytics/top-expenses/{userId}?limit=5`    | Top 5 highest expenses                     |

---

## 📬 4. Notification Service – Email and In-App Alerts

**Responsibilities:**
- Trigger alerts when spending limits are exceeded or to send monthly summaries (email or system notification)

**Endpoints:**

| Method | Endpoint                               | Description                           |
|--------|----------------------------------------|---------------------------------------|
| POST   | `/notifications/settings`              | Configure user notification settings  |
| GET    | `/notifications/settings/{userId}`     | View notification settings            |
| PUT    | `/notifications/settings/{userId}`     | Update settings                       |
| POST   | `/notifications/test-send/{userId}`    | Send a test notification              |
| POST   | `/notifications/trigger-limit-check`   | Trigger alert if user exceeds limit   |
| POST   | `/notifications/monthly-summary/{userId}` | Send monthly summary via email     |

**Technical Details:**

- **Asynchronous notification system using Kafka**:
  - Topic: `expense.limit-exceeded`
  - Topic: `monthly.expense.summary`
- **Delivery channels**:
  - Email (via SMTP, Mailtrap, or SendGrid)
  - In-app system notifications (e.g., toast alerts)

---

## 🔄 Service Communication (High-Level Flow)

1. **When an expense is created →** Kafka sends `limit-exceeded` event  
2. **At the end of the month →** Analytics service triggers `monthly-summary` event  
3. **Notification service →** Sends email or toast alerts  
4. **Frontend →** Communicates through API Gateway to reach all services

---

## 🚀 Recommendations for Expansion

- Use Docker Compose for isolated service deployments  
- Integrate Grafana + Prometheus for metrics monitoring  
- Add centralized logging via the ELK Stack (ElasticSearch + Logstash + Kibana)  
- Visualize analytics in frontend using React.js and Chart.js

---

## 📊 Grafana + Prometheus (Metric Monitoring)

These tools allow real-time tracking of backend microservice performance.

**Examples:**
- How many times per day is `ExpenseService` called?
- What is the average response time of endpoints?
- Which API is most frequently used?
- How do memory and CPU usage vary?

**How it works:**
- **Prometheus** collects metrics from each service
- **Grafana** visualizes them in dashboards

**Example Metrics:**
- `http_server_requests_seconds_count`
- `jvm_memory_used_bytes`
- `process_cpu_usage`

> Requires: `spring-boot-actuator` + `micrometer`

---

## 📂 ELK Stack (Centralized Logging)

- **ELK = ElasticSearch + Logstash + Kibana**
- Purpose: Collect and analyze logs from all services in one place

**Use Cases:**
- Track error logs (NotFound, Exception, Warning)
- Identify most frequent errors
- Trace logs by service and endpoint

**Components:**
- **Logstash**: Aggregates logs
- **ElasticSearch**: Stores logs
- **Kibana**: Provides filtering, search, and visualization

---

### ✨ Why It Matters

- Builds professional, observable backend systems  
- Moves project closer to production standards  
- Shows senior-level awareness of monitoring and infrastructure
