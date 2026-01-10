# Spring Boot Advanced REST API – Reference Sample

This sample is designed **only to validate whether your REST API knowledge is correct and industry-aligned** (not beginner CRUD). You can directly compare this with your existing projects.

---

## 1. Domain Example: User Management (Enterprise Style)

### Base URL

```
/api/v1/users
```

---

## 2. Resource Naming (Plural, Nested, Clean)

### ✅ Correct

```
GET    /api/v1/users
GET    /api/v1/users/{userId}
POST   /api/v1/users
PUT    /api/v1/users/{userId}
PATCH  /api/v1/users/{userId}
DELETE /api/v1/users/{userId}

GET    /api/v1/users/{userId}/roles
POST   /api/v1/users/{userId}/roles
```

### ❌ Wrong

```
/getUser
/createUser
/user/update
```

**Rule:**

* Nouns, not verbs
* Always plural
* Relationships are nested

---

## 3. Idempotency – PUT vs PATCH

### PUT (Replace full resource – idempotent)

```http
PUT /api/v1/users/101
```

```json
{
  "name": "Kamal",
  "email": "kamal@mail.com",
  "status": "ACTIVE"
}
```

Calling this multiple times results in **same state**.

---

### PATCH (Partial update – not necessarily idempotent)

```http
PATCH /api/v1/users/101
```

```json
{
  "status": "SUSPENDED"
}
```

Used for **partial changes only**.

---

## 4. Pagination / Sorting / Filtering (Industry Standard)

### Request

```http
GET /api/v1/users?page=0&size=10&sort=createdAt,desc&status=ACTIVE
```

### Controller

```java
@GetMapping
public Page<UserResponse> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "createdAt,desc") String sort,
    @RequestParam(required = false) UserStatus status
) {
    return userService.getUsers(page, size, sort, status);
}
```

---

## 5. Versioning Strategy (Preferred for Enterprise)

### URL Versioning (Most Common)

```
/api/v1/users
/api/v2/users
```

### ❌ Avoid

* Header-based versioning (hard to debug)
* Media-type versioning (complex)

---

## 6. Error Response Standardization (MOST DEVS FAIL HERE)

### Global Error Response Format

```json
{
  "timestamp": "2025-01-10T10:30:00",
  "status": 400,
  "errorCode": "USER_001",
  "message": "Validation failed",
  "path": "/api/v1/users",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

---

## 7. Global Exception Handler (MANDATORY)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                             HttpServletRequest request) {
        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new FieldErrorResponse(err.getField(), err.getDefaultMessage()))
                .toList();

        ApiErrorResponse response = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode("USER_001")
                .message("Validation failed")
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
```

---

## 8. Error Response DTOs

```java
@Data
@Builder
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String errorCode;
    private String message;
    private String path;
    private List<FieldErrorResponse> errors;
}
```

```java
@AllArgsConstructor
@Data
public class FieldErrorResponse {
    private String field;
    private String message;
}
```

---

## 9. Validation Example

```java
public class CreateUserRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
```

---

## 10. How to Self-Check Your Existing Projects

Ask yourself:

* ❓ Do all APIs return **same error format**?
* ❓ Do I use PUT and PATCH correctly?
* ❓ Is pagination implemented everywhere?
* ❓ Do I avoid verbs in URLs?
* ❓ Do I version APIs properly?

If YES → You are **above average**.
If NO → This is your upgrade path.

---

## 11. Why This Matters for Japan SE Interviews

Japan interviews focus on:

* Predictability
* Consistency
* Maintainability

This design matches **real Japanese enterprise backend standards**.

---

## 12. Kafka Event‑Driven Version (Same API)

### Goal

Keep the **same REST API**, but publish domain events to Kafka so other services react asynchronously.

---

### Architecture

```
Client
  ↓ REST
User Service (Spring Boot)
  ├─ DB (users)
  └─ Kafka Producer → user.events

Audit Service (Kafka Consumer)
Notification Service (Kafka Consumer)
```

---

### When to Publish Events

* User created
* User updated
* User deleted

---

### Event Model

```json
{
  "eventId": "uuid",
  "eventType": "USER_CREATED",
  "timestamp": "2025-01-10T10:30:00",
  "data": {
    "userId": 101,
    "email": "kamal@mail.com"
  }
}
```

---

### Kafka Producer Example

```java
@Component
@RequiredArgsConstructor
public class UserEventProducer {

  private final KafkaTemplate<String, UserEvent> kafkaTemplate;

  public void publishUserCreated(User user) {
    UserEvent event = UserEvent.created(user);
    kafkaTemplate.send("user.events", event.getEventId(), event);
  }
}
```

---

### Service Layer Integration

```java
@Transactional
public User createUser(CreateUserRequest request) {
  User user = repository.save(mapper.toEntity(request));
  userEventProducer.publishUserCreated(user);
  return user;
}
```

---

### What This Proves (Interview Value)

* Event‑driven architecture
* Loose coupling
* Async processing
* Enterprise scalability mindset

---

## 13. gRPC + REST Hybrid Version (Same Domain)

### Goal

Expose **REST for external clients** and **gRPC for internal service‑to‑service communication**.

---

### Architecture

```
Client → REST → API Gateway
               ↓ gRPC
           User Service
               ↓ gRPC
        Notification Service
```

---

### gRPC Contract (Proto)

```proto
syntax = "proto3";

service UserGrpcService {
  rpc GetUserById (UserRequest) returns (UserResponse);
}

message UserRequest {
  int64 userId = 1;
}

message UserResponse {
  int64 userId = 1;
  string email = 2;
  string status = 3;
}
```

---

### gRPC Server Implementation

```java
@GrpcService
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

  @Override
  public void getUserById(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    User user = userService.findById(request.getUserId());

    UserResponse response = UserResponse.newBuilder()
        .setUserId(user.getId())
        .setEmail(user.getEmail())
        .setStatus(user.getStatus().name())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
```

---

### REST Controller Still Exists

```java
@GetMapping("/api/v1/users/{id}")
public UserResponse getUser(@PathVariable Long id) {
  return userService.getUser(id);
}
```

---

### Why Hybrid Is Important

* REST → client‑friendly
* gRPC → high‑performance internal calls
* Common in **large Japanese enterprises**

---

## 14. GitHub‑Ready Project Structure

```
user-service
 ├─ controller (REST)
 ├─ grpc
 ├─ service
 ├─ repository
 ├─ kafka
 ├─ dto
 ├─ exception
 └─ config
```

---

## 15. How to Explain This in Interviews (Japan Style)

Key sentence:

> "We expose REST APIs for external clients, while using Kafka for asynchronous workflows and gRPC for internal high‑performance communication."

This shows **senior‑level architectural thinking**.

---

If you want next:

* Kafka consumer implementations
* Docker Compose (Kafka + Zookeeper)
* Interview Q&A based on this architecture
* README.md for GitHub

Tell me the next step.

## 16. Full Backend-Only Practice Task (NO FRONTEND)

This section is **exactly** what you asked for: a **clear backend task**, entities with attributes, and business flows so you can code and self-check.

---

## Project: Loan Management System (Enterprise Style)

### Scope

* Backend only (Spring Boot)
* REST + Kafka + gRPC
* No UI needed (Postman is enough)

---

## Core Entities (START WITH THESE)

### 1️⃣ User

```java
User
- id (Long)
- username (String)
- email (String)
- role (ADMIN, OFFICER)
- status (ACTIVE, SUSPENDED)
- createdAt (LocalDateTime)
```

---

### 2️⃣ Customer

```java
Customer
- id (Long)
- fullName (String)
- nic (String)
- phone (String)
- address (String)
- status (ACTIVE, BLACKLISTED)
- createdAt (LocalDateTime)
```

---

### 3️⃣ Loan

```java
Loan
- id (Long)
- loanNumber (String)
- customerId (Long)
- principalAmount (BigDecimal)
- interestRate (BigDecimal)
- loanStatus (PENDING, APPROVED, REJECTED, CLOSED)
- createdAt (LocalDateTime)
- version (Long)  // optimistic locking
```

---

### 4️⃣ Payment

```java
Payment
- id (Long)
- loanId (Long)
- amount (BigDecimal)
- paymentDate (LocalDateTime)
- paymentType (CASH, BANK)
```

---

## REST APIs You MUST Implement

### Customer APIs

```
POST   /api/v1/customers
GET    /api/v1/customers/{id}
GET    /api/v1/customers?page=&size=&status=
```

---

### Loan APIs

```
POST   /api/v1/loans
PATCH  /api/v1/loans/{id}/approve
PATCH  /api/v1/loans/{id}/reject
GET    /api/v1/loans?page=&size=&status=
```

---

### Payment APIs

```
POST /api/v1/loans/{loanId}/payments
GET  /api/v1/loans/{loanId}/payments
```

---

## Business Rules (VERY IMPORTANT)

* Loan can be approved **only once**
* Rejected loan cannot receive payments
* Payment updates loan balance
* Optimistic locking must prevent double approval

---

## Kafka Events (PRODUCER SIDE)

### Publish Events On:

* Loan Approved
* Loan Rejected
* Payment Received

### Topic

```
loan.events
```

### Event Example

```json
{
  "eventType": "LOAN_APPROVED",
  "loanId": 1001,
  "timestamp": "2025-01-10T10:30:00"
}
```

---

## Kafka Consumer Tasks (SEPARATE SERVICE)

### Audit Service

* Consume events
* Save audit logs

```java
AuditLog
- id
- eventType
- referenceId
- createdAt
```

---

## gRPC Task (INTERNAL ONLY)

### Purpose

* Fetch loan summary internally

### gRPC Method

```
rpc GetLoanSummary (LoanRequest) returns (LoanSummaryResponse)
```

### Response Fields

```java
- loanId
- customerName
- principalAmount
- loanStatus
```

---

## Project Structure (MANDATORY)

```
loan-service
 ├─ controller
 ├─ service
 ├─ repository
 ├─ kafka
 ├─ grpc
 ├─ entity
 ├─ dto
 ├─ exception
 └─ config
```
