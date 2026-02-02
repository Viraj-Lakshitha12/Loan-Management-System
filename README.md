# 🏦 Loan Management System

> Enterprise-grade Spring Boot application with REST APIs, Kafka event streaming, JWT authentication, and production-ready patterns.

## 🚀 Tech Stack

### Backend
- **Spring Boot 3.2.4** - Core framework
- **Spring Security** - JWT authentication
- **Spring Data JPA** - Database ORM
- **MapStruct** - DTO mapping
- **MySQL** - Relational database

### Event-Driven Architecture
- **Apache Kafka** - Event streaming
- **Zookeeper** - Kafka coordination
- **Docker Compose** - Local development

### Code Quality
- **Lombok** - Reduce boilerplate
- **Global Exception Handler** - Standardized error responses
- **Circuit Breaker Pattern** - Graceful degradation

---

## 📁 Project Structure
```
loan-management-system/
├── config/
│   ├── kafka/          # Kafka producer/consumer config
│   ├── security/       # JWT, Security filters
│   └── mapper/         # MapStruct mappers
├── controller/         # REST endpoints
├── service/
│   ├── impl/          # Business logic
│   └── kakfa/         # Kafka producers/consumers
├── entity/            # JPA entities
├── repo/              # Spring Data repositories
├── dto/
│   ├── request/       # API request DTOs
│   └── response/      # API response DTOs
├── event/             # Kafka event models
├── advisor/           # Global exception handler
└── enums/             # Application enums
```

---

## 🎯 Features

### ✅ Implemented

#### 1. **User Management**
- JWT-based authentication
- Role-based access control (ADMIN, OFFICER)
- User registration and login

#### 2. **Customer Management**
- Create and retrieve customers
- Customer status management
- Pagination support

#### 3. **Loan Management**
- Create loan applications
- Approve/Reject loans (with business rules)
- Loan status tracking
- **Optimistic locking** to prevent double approval

#### 4. **Payment Processing**
- Record payments for approved loans
- Payment validation (only for APPROVED loans)
- Payment history tracking

#### 5. **Event-Driven Architecture (Kafka)**
- **Loan Approved Event** → Audit log, notifications
- **Payment Received Event** → Audit log, accounting updates
- Asynchronous processing for scalability

#### 6. **Production-Ready Patterns**
- Global exception handling
- Standardized API responses
- Circuit breaker for Kafka (graceful degradation)
- Health check service
- Comprehensive logging

---

## 🔥 Key Business Rules

1. ✅ Loan can be approved **only once**
2. ✅ Rejected loan **cannot be approved**
3. ✅ Payments **only allowed for approved loans**
4. ✅ Optimistic locking prevents concurrent modifications

---

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Docker & Docker Compose

### 1. Clone Repository
```bash
git clone https://github.com/Viraj-Lakshitha12/Loan-Management-System.git
cd loan-management-system
```

### 2. Configure Database
Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/loan_management_system
    username: root
    password: your_password
```

### 3. Start Kafka (Docker Compose)
```bash
docker-compose up -d
```

### 4. Run Application
```bash
mvn clean install
mvn spring-boot:run
```

### 5. Test with Postman
Import `postman_collection.json` (if provided)

---

## 📡 API Endpoints

### Authentication
```
POST /api/v1/users/register  - Register new user
POST /api/v1/users/login     - Login (returns JWT)
```

### Customers
```
POST /api/v1/customers       - Create customer
GET  /api/v1/customers/{id}  - Get customer by ID
```

### Loans
```
POST  /api/v1/loans                 - Create loan
GET   /api/v1/loans/{id}            - Get loan details
PATCH /api/v1/loans/{id}/approve    - Approve loan (triggers Kafka event)
PATCH /api/v1/loans/{id}/reject     - Reject loan
```

### Payments
```
POST /api/v1/payments        - Record payment (triggers Kafka event)
GET  /api/v1/payments/{id}   - Get payment details
```

---

## 🎯 Kafka Topics

| Topic | Event | Consumer Action |
|-------|-------|----------------|
| `loan-approved` | Loan approval | Save audit log, send notifications |
| `payment-received` | Payment recorded | Save audit log, update accounting |

---

## 🔒 Security

- JWT tokens (Bearer authentication)
- Password encryption (BCrypt)
- Role-based access control
- Public endpoints: `/api/v1/users/**`
- Protected endpoints: All others

---

## 📊 Database Schema

### Main Tables
- `users` - Application users
- `customers` - Loan customers
- `loans` - Loan applications
- `payments` - Payment records
- `audit_logs` - Event audit trail

---

## 🚀 What Makes This Production-Ready?

1. ✅ **Event-Driven Architecture** (Kafka)
2. ✅ **Asynchronous Processing** (Non-blocking)
3. ✅ **Circuit Breaker Pattern** (Fault tolerance)
4. ✅ **Optimistic Locking** (Concurrency control)
5. ✅ **Global Exception Handling** (Consistent errors)
6. ✅ **Structured Logging** (Monitoring-ready)
7. ✅ **Security Best Practices** (JWT, BCrypt)
8. ✅ **Clean Architecture** (Separation of concerns)

---

## 📈 Future Enhancements

### Next Phase
- [ ] gRPC for internal service communication
- [ ] Redis caching for frequent queries
- [ ] Elasticsearch for log aggregation
- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Kubernetes deployment

### Advanced Features
- [ ] Rate limiting
- [ ] Distributed tracing (Sleuth + Zipkin)
- [ ] Metrics (Prometheus + Grafana)
- [ ] Multi-tenancy support

---

## 🎓 Learning Value

This project demonstrates:
- Senior-level Spring Boot patterns
- Microservices architecture fundamentals
- Event-driven design
- Production-ready code quality
- Industry best practices

**Suitable for:**
- Mid-to-Senior Java developer interviews
- Microservices architecture discussions
- Real-world project portfolio

---

## 📞 Contact

Your Name - [Viraj Lakshitha](mailto:your.viraj.lakshitha.22222@gmail.com)

GitHub: [@Viraj-Lakshitha12](https://github.com/Viraj-Lakshitha12)

LinkedIn: [@viraj-lakshitha01](https://www.linkedin.com/in/viraj-lakshitha01/)

---

## 📄 License

This project is licensed under the MIT License.