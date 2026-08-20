# E-commerce REST API
A complete **E-commerce REST API** built using **Java, Spring Boot, Spring Data JPA, MySQL and REST principles**.

This project is designed as a learning and reference project covering real-world backend development concepts such as:

- Proper package structure
- REST API development
- Entity relationships
- DTO pattern
- Request DTO / Response DTO
- Validation
- JPQL queries
- Native SQL queries
- Named queries
- Enum classes
- Custom exceptions
- Global exception handling
- JPA auditing
- Transaction management
- Scheduler
- Swagger / OpenAPI
- Logging
- API versioning
- Spring Boot Actuator
- Lombok
- Product image upload
- MySQL
- Complete CRUD operations

---

# 1. Project Overview

## Project Name
**E-commerce REST API**

## Technology Stack

| Technology | Version / Usage |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.3 |
| Spring Web | REST API |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| MySQL | Relational database |
| Maven | Build management |
| Lombok | Boilerplate reduction |
| Jakarta Validation | Request validation |
| Swagger / OpenAPI | API documentation |
| Spring Boot Actuator | Application monitoring |
| SLF4J | Logging |
| JPA Auditing | createdAt / updatedAt |

---

# 2. Business Context
This REST API is designed for an e-commerce platform.

The application manages:

- Customers
- Products
- Orders
- Order Items
- Shopping Carts
- Cart Items
- Payments

Customers can register and manage their accounts. Products can be added, updated and managed by administrators. Customers can place orders containing multiple products. Customers can add products to shopping carts before checkout. Payments are associated with orders and tracked through the payment module.

The API is designed so that it can later be integrated with a frontend application such as:

- React
- Angular
- Vue
- Mobile applications
- Other REST API consumers

---

# 3. Main Modules
The project contains seven main database modules.

```
1. Customer
2. Product
3. Order
4. OrderItem
5. ShoppingCart
6. CartItem
7. Payment
```

---

# 4. Database Design

## 4.1 Customers

```
customers
------------------------------------------------
id              BIGINT PRIMARY KEY
name            VARCHAR
email           VARCHAR
phone           VARCHAR
address         VARCHAR
created_at      DATETIME
```

**Purpose:** Stores customer information.

---

## 4.2 Products

```
products
------------------------------------------------
id              BIGINT PRIMARY KEY
name            VARCHAR
description     VARCHAR
price           DECIMAL
stock           INTEGER
category        VARCHAR
```

**Purpose:** Stores products available in the e-commerce system.

---

## 4.3 Orders

```
orders
------------------------------------------------
id                  BIGINT PRIMARY KEY
customer_id         BIGINT FOREIGN KEY
order_date          DATETIME
status              VARCHAR
shipping_address    VARCHAR
total_amount        DECIMAL
```

**Purpose:** Stores customer orders.

---

## 4.4 Order Items

```
order_items
------------------------------------------------
id              BIGINT PRIMARY KEY
order_id        BIGINT FOREIGN KEY
product_id      BIGINT FOREIGN KEY
quantity        INTEGER
price           DECIMAL
```

**Purpose:** Stores individual products belonging to an order.

Example:
```
Order #1001

Product A → quantity 2 → price 500
Product B → quantity 1 → price 1000
```

---

## 4.5 Shopping Carts

```
shopping_carts
------------------------------------------------
id              BIGINT PRIMARY KEY
customer_id     BIGINT FOREIGN KEY
created_at      DATETIME
updated_at      DATETIME
```

**Purpose:** Stores customer shopping carts.

---

## 4.6 Cart Items

```
cart_items
------------------------------------------------
id              BIGINT PRIMARY KEY
cart_id         BIGINT FOREIGN KEY
product_id      BIGINT FOREIGN KEY
quantity        INTEGER
```

**Purpose:** Stores products added to a shopping cart.

---

## 4.7 Payments

```
payments
------------------------------------------------
id              BIGINT PRIMARY KEY
order_id        BIGINT FOREIGN KEY
payment_date    DATETIME
amount          DECIMAL
method          VARCHAR
status          VARCHAR
```

**Purpose:** Stores payment information associated with an order.

---

# 5. Entity Relationships

## Customer → Orders
One customer can have multiple orders.

```
Customer
    |
    | 1
    |
    | *
    ↓
Orders
```

JPA:
```java
@OneToMany(mappedBy = "customer")
private List<Order> orders;
```

---

## Order → Order Items
One order can contain multiple order items.

```
Order
    |
    | 1
    |
    | *
    ↓
OrderItem
```

---

## Product → Order Items
One product can appear in multiple order items.

```
Product
    |
    | 1
    |
    | *
    ↓
OrderItem
```

Therefore:
```
Orders
   ↕
OrderItems
   ↕
Products
```

This represents a Many-to-Many relationship: `Orders ↔ Products` through `OrderItems`.

---

## Customer → Shopping Cart
A customer can have shopping carts.

```
Customer
    |
    | 1
    |
    | *
    ↓
ShoppingCart
```

---

## Shopping Cart → Cart Items
A shopping cart contains multiple cart items.

```
ShoppingCart
      |
      | 1
      |
      | *
      ↓
CartItem
```

---

## Product → Cart Items
A product can appear in multiple cart items.

Therefore:
```
ShoppingCart
      ↕
   CartItem
      ↕
    Product
```

This represents a Many-to-Many relationship: `ShoppingCart ↔ Product` through `CartItem`.

---

## Order → Payment
Each order has one payment.

```
Order
  |
  | 1 : 1
  |
  ↓
Payment
```

---

# 6. Complete Relationship Diagram

```
                     ┌───────────────┐
                     │   Customers   │
                     └───────┬───────┘
                             │
                          1  │
                             │ *
                     ┌───────▼───────┐
                     │     Orders    │
                     └───────┬───────┘
                             │
                          1  │
                             │ *
                     ┌───────▼───────┐
                     │  OrderItems   │
                     └───────┬───────┘
                             │
                          *  │
                             │ 1
                     ┌───────▼───────┐
                     │    Products   │
                     └───────────────┘

                     ┌───────────────┐
                     │   Customers   │
                     └───────┬───────┘
                             │
                          1  │
                             │ *
                     ┌───────▼───────┐
                     │ ShoppingCart  │
                     └───────┬───────┘
                             │
                          1  │
                             │ *
                     ┌───────▼───────┐
                     │   CartItem    │
                     └───────┬───────┘
                             │
                          *  │
                             │ 1
                     ┌───────▼───────┐
                     │    Products   │
                     └───────────────┘

                     ┌───────────────┐
                     │     Orders    │
                     └───────┬───────┘
                             │
                          1  │
                             │ 1
                     ┌───────▼───────┐
                     │    Payments   │
                     └───────────────┘
```

---

# 7. Project Architecture
The application follows a layered architecture:

**Request Flow:**
```
Client
  ↓
Controller
  ↓
Request DTO
  ↓
Service
  ↓
Repository
  ↓
Entity
  ↓
Database
```

**Response Flow:**
```
Database
  ↓
Entity
  ↓
Service
  ↓
Response DTO
  ↓
Controller
  ↓
Client
```

---

# 8. Complete Package Structure

```
com.example.ecommerce
│
├── EcommerceApplication.java
│
├── config
│   └── OpenApiConfig.java
│
├── controller
│   ├── v1
│   │   ├── CustomerController.java
│   │   ├── ProductController.java
│   │   ├── OrderController.java
│   │   ├── OrderItemController.java
│   │   ├── ShoppingCartController.java
│   │   ├── CartItemController.java
│   │   └── PaymentController.java
│   │
│   └── v2
│       └── ProductControllerV2.java
│
├── dto
│   │
│   ├── customer
│   │   ├── CustomerRequestDto.java
│   │   └── CustomerResponseDto.java
│   │
│   ├── product
│   │   ├── ProductRequestDto.java
│   │   └── ProductResponseDto.java
│   │
│   ├── order
│   │   ├── OrderRequestDto.java
│   │   └── OrderResponseDto.java
│   │
│   ├── orderitem
│   │   ├── OrderItemRequestDto.java
│   │   └── OrderItemResponseDto.java
│   │
│   ├── cart
│   │   ├── ShoppingCartRequestDto.java
│   │   └── ShoppingCartResponseDto.java
│   │
│   ├── cartitem
│   │   ├── CartItemRequestDto.java
│   │   └── CartItemResponseDto.java
│   │
│   └── payment
│       ├── PaymentRequestDto.java
│       └── PaymentResponseDto.java
│
├── entity
│   ├── BaseEntity.java
│   ├── Customer.java
│   ├── Product.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── ShoppingCart.java
│   ├── CartItem.java
│   ├── Payment.java
│   │
│   ├── ProductCategory.java
│   ├── OrderStatus.java
│   ├── PaymentMethod.java
│   └── PaymentStatus.java
│
├── repository
│   ├── CustomerRepository.java
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   ├── OrderItemRepository.java
│   ├── ShoppingCartRepository.java
│   ├── CartItemRepository.java
│   └── PaymentRepository.java
│
├── service
│   ├── CustomerService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── OrderItemService.java
│   ├── ShoppingCartService.java
│   ├── CartItemService.java
│   └── PaymentService.java
│
├── service/impl
│   ├── CustomerServiceImpl.java
│   ├── ProductServiceImpl.java
│   ├── OrderServiceImpl.java
│   ├── OrderItemServiceImpl.java
│   ├── ShoppingCartServiceImpl.java
│   ├── CartItemServiceImpl.java
│   └── PaymentServiceImpl.java
│
├── exception
│   ├── ApiError.java
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   ├── InsufficientStockException.java
│   ├── InvalidOrderException.java
│   └── GlobalExceptionHandler.java
│
├── scheduler
│   ├── OrderScheduler.java
│   └── CartScheduler.java
│
└── util
    └── FileStorageUtil.java
```

---

# 9. Maven Dependencies

```xml
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-validation
spring-boot-starter-actuator
mysql-connector-j
springdoc-openapi-starter-webmvc-ui
lombok
spring-boot-starter-test
```

---

# 10. Database Configuration

**Database name:** `ecommerce_db`

**Configuration:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: root
    password: root
```

Change the username and password according to your local MySQL installation.

---

# 11. Create Database

Run:
```sql
CREATE DATABASE ecommerce_db;
```

Verify:
```sql
SHOW DATABASES;
```

Then:
```sql
USE ecommerce_db;
```

---

# 12. JPA Configuration

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
```

During development, `ddl-auto: update` can be used to automatically update the schema. For production systems, database migration tools such as Flyway or Liquibase should be considered.

---

# 13. Enum Classes
The project uses enums instead of storing arbitrary strings throughout business logic.

## ProductCategory

```java
public enum ProductCategory {
    ELECTRONICS,
    FASHION,
    HOME,
    BOOKS,
    BEAUTY,
    SPORTS,
    GROCERY
}
```

## OrderStatus

```java
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
```

## PaymentMethod

```java
public enum PaymentMethod {
    CASH_ON_DELIVERY,
    CARD,
    UPI,
    NET_BANKING
}
```

## PaymentStatus

```java
public enum PaymentStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REFUNDED
}
```

Enums improve consistency and reduce invalid status values.

---

# 14. DTO Architecture
Entities should not be directly exposed through REST APIs. The application uses:

**Request Flow:**
```
Request DTO
     ↓
Controller
     ↓
Service
     ↓
Entity
```

**Response Flow:**
```
Entity
   ↓
Service
   ↓
Response DTO
   ↓
Controller
```

---

# 15. Request DTO

Example:
```java
public record ProductRequestDto(
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    ProductCategory category
) {}
```

The Request DTO represents data received from the client.

---

# 16. Response DTO

Example:
```java
public record ProductResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    ProductCategory category
) {}
```

The Response DTO represents data returned to the client.

---

# 17. Validation
Validation is performed using Jakarta Validation.

Example:
```java
@NotBlank(message = "Product name is required")
private String name;

@NotNull(message = "Price is required")
@DecimalMin(value = "0.01")
private BigDecimal price;

@NotNull(message = "Stock is required")
@Min(value = 0)
private Integer stock;
```

Controller:
```java
@PostMapping
public ResponseEntity<ProductResponseDto> create(
        @Valid @RequestBody ProductRequestDto request) {
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.create(request));
}
```

---

# 18. Custom Exceptions

### ResourceNotFoundException
Used when a requested resource does not exist.

Example: `Customer with ID 100 not found`

### DuplicateResourceException
Used when a duplicate resource is detected.

Example: `Customer email already exists`

### InsufficientStockException
Used when an order requests more products than the available stock.

Example: `Insufficient stock for product 10`

### InvalidOrderException
Used for invalid order operations.

Example: `Cannot cancel a delivered order`

---

# 19. Global Exception Handling
The application uses `@RestControllerAdvice` for centralized exception handling. The central exception handler handles:

- 400 Bad Request
- 404 Not Found
- 409 Conflict
- 500 Internal Server Error

Validation errors are returned as field-level errors.

Example:
```json
{
  "timestamp": "2026-08-17T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/products",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than zero"
  }
}
```

---

# 20. Repository Layer
Repositories extend `JpaRepository<Entity, Long>`.

Example:
```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

Spring Data JPA automatically provides:
```
save()
findAll()
findById()
delete()
deleteById()
count()
existsById()
```

---

# 21. JPQL Queries
JPQL operates on entities and entity fields.

Example:
```java
@Query("""
    SELECT p
    FROM Product p
    WHERE p.price BETWEEN :min AND :max
    AND p.status = :status
""")
List<Product> findByPriceRange(
        @Param("min") BigDecimal min,
        @Param("max") BigDecimal max,
        @Param("status") ProductStatus status
);
```

**Important:** JPQL uses entity names (`Product`, `p.price`, `p.status`) rather than database table names.

---

# 22. Native SQL Queries
Native queries use actual database SQL.

Example:
```java
@Query(
    value = "SELECT * FROM products WHERE stock > :minimumStock",
    nativeQuery = true
)
List<Product> findProductsWithStock(
        @Param("minimumStock") int minimumStock
);
```

Here, `products` and `stock` are actual database table/column names.

---

# 23. Named Queries
Named queries are defined on entities.

Example:
```java
@NamedQuery(
    name = "Product.findActiveByCategoryNamed",
    query = """
        SELECT p
        FROM Product p
        WHERE p.category = :category
        AND p.status = 'ACTIVE'
    """
)
```

Repository:
```java
List<Product> findActiveByCategoryNamed(
        @Param("category") ProductCategory category
);
```

The project demonstrates all three query styles: JPQL, Native SQL, and Named Query.

---

# 24. Auditing
JPA auditing automatically maintains `createdAt` and `updatedAt` fields.

Base entity:
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

Application setup:
```java
@EnableJpaAuditing
```

---

# 25. Transaction Management
Transactions are especially important during checkout. Example business process:

```
Create Order
     ↓
Create OrderItem
     ↓
Check Product Stock
     ↓
Reduce Product Stock
     ↓
Calculate Total
     ↓
Create Payment
```

All operations should be treated as one business transaction:

```java
@Transactional
public OrderResponseDto checkout(...) {
    // order creation
    // order item creation
    // stock update
    // payment creation
}
```

If an operation fails, the entire transaction is **ROLLED BACK**, preventing partially completed orders.

---

# 26. Scheduler
The application uses Spring Scheduling.

Enable:
```java
@EnableScheduling
```

Example:
```java
@Scheduled(fixedRate = 300000)
public void cancelExpiredOrders() {
    // find old pending orders
    // change status to CANCELLED
}
```

The scheduler can be used for:

### Pending Order Cleanup
```
PENDING → Older than configured time → CANCELLED
```

### Abandoned Cart Processing
```
Cart not updated → Configured period exceeded → Process abandoned cart
```

The exact scheduler timing should be configured according to business requirements.

---

# 27. Logging
The project uses Lombok `@Slf4j` annotation.

Example:
```java
log.info("Creating product: {}", request.name());
```

Other levels:
```
log.debug("Product details: {}", product);
log.warn("Low stock for product: {}", productId);
log.error("Unable to process order: {}", orderId);
```

**Recommended levels:**
- INFO → Important application events
- DEBUG → Development/debug information
- WARN → Potential problems
- ERROR → Failures/exceptions

---

# 28. Swagger / OpenAPI
Swagger provides interactive API documentation.

**URLs:**
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

Swagger allows developers to:
- View endpoints
- View request bodies
- View response bodies
- Execute APIs
- Test validation
- Test different HTTP methods

---

# 29. API Versioning
The project uses URI-based versioning.

**Version 1:**
```
/api/v1/customers
/api/v1/products
/api/v1/orders
```

**Version 2:**
```
/api/v2/products
```

Versioning allows the API contract to evolve without immediately breaking existing clients.

---

# 30. Actuator
Spring Boot Actuator provides monitoring endpoints.

**Health:**
```
GET /actuator/health
Response: { "status": "UP" }
```

**Information:**
```
GET /actuator/info
```

**Metrics:**
```
GET /actuator/metrics
```

**Loggers:**
```
GET /actuator/loggers
```

Only expose the endpoints required by your environment.

---

# 31. Product Image Upload
Products can optionally have an image.

**Endpoint:**
```
POST /api/v1/products/{id}/image
```

**Request:**
```
Content-Type: multipart/form-data

Key: file
Type: File
Value: <select image>
```

**Example:**
```
POST http://localhost:8080/api/v1/products/1/image
```

The sample project stores uploaded files locally. For production, consider object storage such as AWS S3, Azure Blob Storage, or Google Cloud Storage.

---

# 32. REST API Endpoints

## Customer APIs

### Create Customer
```
POST /api/v1/customers

{
  "name": "Anna Reddy",
  "email": "anna@example.com",
  "phone": "9876543210",
  "address": "Bangalore"
}
```

### Get All Customers
```
GET /api/v1/customers
```

### Get Customer By ID
```
GET /api/v1/customers/{id}
```

### Update Customer
```
PUT /api/v1/customers/{id}
```

### Delete Customer
```
DELETE /api/v1/customers/{id}
```

---

## Product APIs

### Create Product
```
POST /api/v1/products

{
  "name": "Mechanical Keyboard",
  "description": "RGB Mechanical Keyboard",
  "price": 2499.00,
  "stock": 50,
  "category": "ELECTRONICS"
}
```

### Get All Products
```
GET /api/v1/products
```

### Get Product
```
GET /api/v1/products/{id}
```

### Update Product
```
PUT /api/v1/products/{id}
```

### Delete Product
```
DELETE /api/v1/products/{id}
```

### Product Image Upload
```
POST /api/v1/products/{id}/image
```

---

## Order APIs

### Create Order
```
POST /api/v1/orders

{
  "customerId": 1,
  "totalAmount": 4998.00
}
```

Note: In the final checkout implementation, total amount should be calculated from OrderItems rather than blindly trusting a client-supplied total.

### Get All Orders
```
GET /api/v1/orders
```

### Get Order By ID
```
GET /api/v1/orders/{id}
```

### Update Order
```
PUT /api/v1/orders/{id}
```

### Delete Order
```
DELETE /api/v1/orders/{id}
```

---

## Order Item APIs

Recommended endpoints:
```
POST   /api/v1/order-items
GET    /api/v1/order-items
GET    /api/v1/order-items/{id}
PUT    /api/v1/order-items/{id}
DELETE /api/v1/order-items/{id}
```

Order items should normally be created as part of the checkout/order transaction rather than being freely manipulated in every production scenario.

---

## Shopping Cart APIs

```
POST   /api/v1/carts
GET    /api/v1/carts
GET    /api/v1/carts/{id}
PUT    /api/v1/carts/{id}
DELETE /api/v1/carts/{id}
```

---

## Cart Item APIs

Recommended endpoints:
```
POST   /api/v1/cart-items
GET    /api/v1/cart-items
GET    /api/v1/cart-items/{id}
PUT    /api/v1/cart-items/{id}
DELETE /api/v1/cart-items/{id}
```

---

## Payment APIs

```
POST   /api/v1/payments
GET    /api/v1/payments
GET    /api/v1/payments/{id}
PUT    /api/v1/payments/{id}
DELETE /api/v1/payments/{id}
```

Payment processing should eventually be integrated with an actual payment gateway rather than treating the REST endpoint itself as a payment processor.

---

# 33. HTTP Status Codes
The API follows these status codes:

| Status | Meaning |
|---|---|
| 200 | OK / Request succeeded |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

---

# 34. Example Successful Response

```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "price": 2499.00,
  "stock": 50,
  "category": "ELECTRONICS"
}
```

---

# 35. Example 404 Response

```json
{
  "timestamp": "2026-08-17T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found: 100",
  "path": "/api/v1/products/100"
}
```

---

# 36. Example Validation Error

Request:
```json
{
  "name": "",
  "price": -100,
  "stock": -5
}
```

Response:
```json
{
  "timestamp": "2026-08-17T10:35:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/products",
  "validationErrors": {
    "name": "Product name is required",
    "price": "Price must be greater than zero",
    "stock": "Stock cannot be negative"
  }
}
```

---

# 37. Running the Application

## Step 1 — Clone / Open Project
Open the project in IntelliJ IDEA or VS Code.

## Step 2 — Verify Java
```bash
java -version
```
Expected: Java 17

## Step 3 — Verify Maven
```bash
mvn -version
```

## Step 4 — Create Database
```sql
CREATE DATABASE ecommerce_db;
```

## Step 5 — Configure MySQL
Update `application.properties` or `application.yml`:
```yaml
spring:
  datasource:
    username: root
    password: root
```

Update with your actual MySQL credentials.

## Step 6 — Build
```bash
mvn clean install
```

## Step 7 — Run
```bash
mvn spring-boot:run
```

Or run `EcommerceApplication.java` from your IDE.

---

# 38. Application URL
```
http://localhost:8080
```

---

# 39. Swagger URL
```
http://localhost:8080/swagger-ui.html
```

---

# 40. Actuator URLs

```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/loggers
```

---

# 41. Recommended Development Order

Build the application in this order:

```
1. Project Setup
2. Database Configuration
3. Entity Classes
4. Entity Relationships
5. Enum Classes
6. Repository Layer
7. Customer CRUD
8. Product CRUD
9. Order CRUD
10. OrderItem
11. ShoppingCart
12. CartItem
13. Payment
14. Request DTO
15. Response DTO
16. Validation
17. Custom Exceptions
18. Global Exception Handler
19. JPQL
20. Native Queries
21. Named Queries
22. Auditing
23. Transactions
24. Checkout Transaction
25. Scheduler
26. Logging
27. Swagger
28. API Versioning
29. Actuator
30. Image Upload
31. Postman Testing
32. Final Documentation
```

---

# 42. Complete Checkout Flow

The most important business flow is:

```
Customer
   ↓
Add Product
   ↓
Shopping Cart
   ↓
Cart Items
   ↓
Checkout
   ↓
Validate Customer
   ↓
Validate Products
   ↓
Check Stock
   ↓
Create Order
   ↓
Create Order Items
   ↓
Calculate Total
   ↓
Reduce Product Stock
   ↓
Create Payment
   ↓
Commit Transaction
```

If any critical operation fails: **ROLLBACK**

---

# 43. Example Checkout Scenario

**Customer:**
```
Customer ID = 1
```

**Cart:**
```
Product A: Quantity 2, Price ₹500
Product B: Quantity 1, Price ₹1000
```

**Calculation:**
```
Product A: 2 × ₹500 = ₹1000
Product B: 1 × ₹1000 = ₹1000
Total = ₹2000
```

**Order:**
```
Order Status = PENDING
Total Amount = ₹2000
```

**After successful payment:**
```
Order Status = CONFIRMED
Payment Status = SUCCESS
```

---

# 44. Logging Strategy

Recommended logging places:
```
Controller
   ↓
Service
   ↓
Repository
```

Most business logs should be placed in the **service layer**.

Example:
```java
log.info("Creating order for customerId={}", customerId);
```

**Avoid logging sensitive information** such as:
- Passwords
- Card numbers
- Authentication tokens
- Payment secrets

---

# 45. File Upload Structure

Uploaded images can be stored under:
```
uploads/
└── products/
    ├── product-image-1.jpg
    ├── product-image-2.png
    └── ...
```

The database stores the image path/reference. The image itself should not normally be stored directly inside the Product entity as a large binary object unless there is a specific requirement.

---

# 46. Testing Strategy

The project should be tested using:
```
Swagger
Postman
JUnit
Mockito
Integration Tests
```

**Basic testing order:**
```
Customer
   ↓
Product
   ↓
Cart
   ↓
Cart Items
   ↓
Order
   ↓
Order Items
   ↓
Payment
```

---

# 47. Postman Testing Sequence

Use this sequence:
```
1. Create Customer
2. Get Customer
3. Create Product
4. Get Product
5. Update Product
6. Create Shopping Cart
7. Add Cart Item
8. Get Cart
9. Create Order
10. Create Order Items
11. Process Payment
12. Check Order
13. Check Product Stock
14. Test Validation
15. Test 404
16. Test Duplicate Exception
17. Test Scheduler
18. Test Image Upload
```

---

# 48. Query Testing

Test all three query mechanisms.

### JPQL
```
GET /api/v1/products/query/price-range?min=1000&max=5000
```

### Native SQL
```
GET /api/v1/products/query/stock?minimumStock=10
```

### Named Query
```
GET /api/v1/products/query/category?category=ELECTRONICS
```

---

# 49. Version Testing

**V1:**
```
GET /api/v1/products
```

**V2:**
```
GET /api/v2/products
```

When the API contract changes in the future, existing clients remain on V1 while new clients use V2.

---

# 50. Error Testing

### Invalid ID
```
GET /api/v1/products/99999
```
Expected: `404 Not Found`

### Invalid Request
```
POST /api/v1/products

{
  "name": "",
  "price": -100
}
```
Expected: `400 Bad Request`

### Duplicate Customer
Try registering the same email twice.
Expected: `409 Conflict`

---

# 51. Scheduler Testing

For development, configure a short interval:
```java
@Scheduled(fixedRate = 30000)
```

This runs every 30 seconds.

For actual business configuration, use a property:
```yaml
app:
  scheduler:
    order-cancellation-rate: 300000
```

This avoids hard-coding scheduling values.

---

# 52. Production Improvements

This project is designed as a strong learning/reference project. For production, consider adding:

```
JWT Authentication
Role-Based Authorization
Spring Security
Pagination
Sorting
Filtering
Search
Flyway / Liquibase
Docker
Docker Compose
Redis
AWS S3
Payment Gateway
Email Notifications
Kafka / RabbitMQ
Caching
Unit Tests
Integration Tests
CI/CD
Environment-specific configuration
Secrets Management
API Rate Limiting
Database Indexing
```

---

# 53. Future Module Expansion

The architecture allows additional modules to be added without disturbing the existing structure.

Possible future packages:
```
auth
security
notification
wishlist
review
coupon
category
inventory
shipping
delivery
```

---

# 54. Important Design Rules

### Rule 1
Do not expose JPA entities directly from controllers. Use Request DTO and Response DTO.

### Rule 2
Business logic belongs in Service, not Controller.

### Rule 3
Database operations belong in Repository.

### Rule 4
Controllers should mainly handle:
- HTTP request
- HTTP response
- validation
- routing

### Rule 5
Transactions should be placed around business operations (e.g., Checkout) rather than unnecessarily making every method transactional.

### Rule 6
Use custom exceptions for expected business errors.

### Rule 7
Use enums for fixed business states.

### Rule 8
Use logging instead of `System.out.println()`.

---

# 55. Git Structure

Recommended Git branches:
```
main
develop
feature/customer
feature/product
feature/order
feature/cart
feature/payment
feature/swagger
feature/scheduler
feature/file-upload
```

Example:
```bash
git checkout -b feature/product
git add .
git commit -m "Implement product CRUD APIs"
```

---

# 56. Useful Maven Commands

```bash
mvn clean                   # Clean project
mvn compile                 # Compile
mvn test                    # Run tests
mvn package                 # Package
mvn clean package           # Clean and package
mvn spring-boot:run         # Run Spring Boot
```

---

# 57. Common Problems

## MySQL connection error
Check:
- MySQL service is running
- Database name is correct
- Username and password are correct
- Port is 3306

## Port already in use
Change port in `application.properties`:
```properties
server.port=8081
```

## Lombok not working
Check:
- IntelliJ: Settings → Plugins → Lombok
- Enable annotation processing: Settings → Build, Execution, Deployment → Compiler → Annotation Processors

## Table not created
Check:
- `spring.jpa.hibernate.ddl-auto: update` is configured
- Database connection is working

---

# 58. Project Flow Summary

**Complete Architecture:**
```
                E-COMMERCE REST API
                       │
        ┌──────────────┼──────────────┐
        │              │              │
    Customer        Product         Order
        │              │              │
        │              │          OrderItem
        │              │              │
        │              └──────────────┘
        │
   ShoppingCart
        │
    CartItem
        │
     Product

     Order
       │
    Payment
```

**Application Architecture:**
```
                Client
                  │
                  ▼
              Controller
                  │
                  ▼
              Request DTO
                  │
                  ▼
               Service
                  │
          ┌───────┴───────┐
          │               │
    Business Logic   Transaction
          │               │
          └───────┬───────┘
                  ▼
             Repository
                  │
                  ▼
                JPA
                  │
                  ▼
               MySQL
```

**Supporting Components:**
```
    ┌───────────────┐
    │   Scheduler   │
    └───────┬───────┘
            │
            ▼
        Service

    ┌───────────────┐
    │    Swagger    │
    └───────────────┘

    ┌───────────────┐
    │   Actuator    │
    └───────────────┘

    ┌───────────────┐
    │    Logger     │
    └───────────────┘

    ┌───────────────┐
    │    Global     │
    │   Exception   │
    │    Handler    │
    └───────────────┘
```

---

# 59. Final Checklist

Before considering the project complete:

- [ ] Project starts successfully
- [ ] MySQL connection works
- [ ] All seven entities created
- [ ] Entity relationships verified
- [ ] Enum classes implemented
- [ ] Customer CRUD completed
- [ ] Product CRUD completed
- [ ] Order CRUD completed
- [ ] OrderItem implemented
- [ ] ShoppingCart implemented
- [ ] CartItem implemented
- [ ] Payment implemented
- [ ] Request DTOs implemented
- [ ] Response DTOs implemented
- [ ] Validation implemented
- [ ] Custom exceptions implemented
- [ ] Global exception handler implemented
- [ ] JPQL query implemented
- [ ] Native SQL query implemented
- [ ] Named query implemented
- [ ] Auditing implemented
- [ ] Transaction management implemented
- [ ] Checkout transaction implemented
- [ ] Scheduler implemented
- [ ] Logging implemented
- [ ] Swagger implemented
- [ ] API versioning implemented
- [ ] Actuator implemented
- [ ] Product image upload implemented
- [ ] Postman testing completed
- [ ] Unit tests added
- [ ] Integration tests added
- [ ] README updated

---

# 60. Final Goal

The final project should demonstrate that a complete Spring Boot E-commerce backend can be designed using:

```
Java
+
Spring Boot
+
REST API
+
Spring Data JPA
+
Hibernate
+
MySQL
+
DTO
+
Validation
+
Exception Handling
+
JPQL
+
Native Query
+
Named Query
+
Relationships
+
Enum
+
Auditing
+
Transactions
+
Scheduler
+
Swagger
+
Logging
+
Versioning
+
Actuator
+
Lombok
+
File Upload
```

The architecture should remain easy to modify when adding new business requirements.

For every new module, follow the same pattern:
```
Entity
  ↓
Repository
  ↓
Request DTO
  ↓
Response DTO
  ↓
Service Interface
  ↓
Service Implementation
  ↓
Controller
  ↓
Exception Handling
  ↓
Swagger
  ↓
Postman Testing
  ↓
README Update
```

---

# 61. Maintenance Notes

Whenever the project is modified:

1. Update the entity
2. Update the database relationship if necessary
3. Update Request DTO
4. Update Response DTO
5. Update Repository queries
6. Update Service interface
7. Update Service implementation
8. Update Controller
9. Update Swagger documentation
10. Add/update validation
11. Add/update exception handling
12. Add Postman test
13. Update this README

This README should be treated as the **living documentation** of the project.

---

# 62. Version History

## Version 1.0.0
Initial E-commerce REST API architecture.

Included:
- Customer
- Product
- Order
- OrderItem
- ShoppingCart
- CartItem
- Payment
- REST API
- DTO architecture
- Validation
- Exception handling
- JPA queries
- Auditing
- Transactions
- Scheduler
- Swagger
- Logging
- Versioning
- Actuator
- Image upload

---

# 63. Author / Project Information

**Project:** E-commerce REST API

**Backend:** Spring Boot

**Language:** Java

**Database:** MySQL

**Architecture:** Layered REST Architecture

**API Style:** RESTful API

**Documentation:** Swagger / OpenAPI

**Build Tool:** Maven

---

## License
This project is open-source and available for educational purposes.

---

## Contact & Support
For questions or support, please refer to the project documentation or open an issue in the repository.
# ecommerce-rest-api-final
