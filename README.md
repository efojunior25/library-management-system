# 📚 Library Management System

A comprehensive library management system built with **Spring Boot 3**, **Java 17**, and modern web technologies.

## 🚀 Features

### 📖 Book Management
- ✅ Add, update, and delete books
- ✅ ISBN-based unique identification
- ✅ Category management
- ✅ Advanced search functionality
- ✅ Availability tracking
- ✅ Popular books reporting

### 👤 User Management
- ✅ User registration and profile management
- ✅ Unique user codes
- ✅ Status management (Active, Inactive, Suspended)
- ✅ Contact information tracking
- ✅ Loan history

### 📋 Loan System
- ✅ Book borrowing with 14-day default period
- ✅ Automatic fine calculation for overdue books
- ✅ Loan renewal functionality
- ✅ Return processing
- ✅ Overdue tracking and reporting

### 📊 Reporting & Analytics
- ✅ Most popular books
- ✅ Overdue loans report
- ✅ User activity statistics
- ✅ Library inventory status

## 🛠️ Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2+**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **Lombok**

### Database
- **H2** (Development)
- **MySQL** (Production)

### Frontend
- **Thymeleaf**
- **Bootstrap 5**
- **HTML5/CSS3**
- **JavaScript**

### Tools & DevOps
- **Maven**
- **Git Flow**
- **Spring Boot Actuator**
- **Spring DevTools**

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/biblioteca/management/
│   │   ├── LibraryManagementSystemApplication.java
│   │   ├── config/                 # Configuration classes
│   │   ├── controller/            # REST & Web controllers
│   │   │   └── web/              # Thymeleaf controllers
│   │   ├── dto/                   # Data Transfer Objects
│   │   ├── model/                 # JPA Entities
│   │   ├── repository/            # Data repositories
│   │   ├── service/               # Business logic
│   │   └── exception/             # Exception handlers
│   └── resources/
│       ├── static/                # CSS, JS, Images
│       ├── templates/             # Thymeleaf templates
│       └── application.yml        # Configuration
└── test/                          # Unit & Integration tests
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/efojunior25/library-management-system.git
   cd library-management-system
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**
    - **Web Interface**: http://localhost:8080
    - **H2 Console**: http://localhost:8080/h2-console
    - **API Documentation**: http://localhost:8080/swagger-ui.html
    - **Health Check**: http://localhost:8080/actuator/health

### Default H2 Database Configuration
```
URL: jdbc:h2:mem:library
Username: sa
Password: (empty)
```

## 🔧 Configuration

### Development Profile
```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:h2:mem:library-dev
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
```

### Production Profile
```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://localhost:3306/library
    username: ${DB_USERNAME:library_user}
    password: ${DB_PASSWORD:your_password}
  jpa:
    hibernate:
      ddl-auto: validate
```

## 📡 API Endpoints

### Books API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books/isbn/{isbn}` | Get book by ISBN |
| POST | `/api/books` | Create new book |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |
| GET | `/api/books/search?term={term}` | Search books |
| GET | `/api/books/available` | Available books |
| GET | `/api/books/popular` | Popular books |

### Users API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/code/{code}` | Get user by code |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Loans API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/loans` | List all loans |
| GET | `/api/loans/{id}` | Get loan by ID |
| POST | `/api/loans` | Create new loan |
| PUT | `/api/loans/{id}/return` | Return book |
| PUT | `/api/loans/{id}/renew` | Renew loan |
| GET | `/api/loans/overdue` | Overdue loans |
| GET | `/api/loans/user/{userId}` | User loans |

## 🌐 Web Interface Routes

| Route | Description |
|-------|-------------|
| `/` | Dashboard |
| `/books` | Book management |
| `/books/new` | Add new book |
| `/books/{id}/edit` | Edit book |
| `/users` | User management |
| `/users/new` | Add new user |
| `/loans` | Loan management |
| `/loans/new` | Create new loan |
| `/reports` | Reports dashboard |

## 💾 Database Schema

### Core Entities

#### Books
- `id` (Primary Key)
- `isbn` (Unique)
- `title`
- `author`
- `category`
- `available`
- `times_borrowed`
- `publication_date`
- `description`
- `page_count`
- `publisher`
- Timestamps (created_at, updated_at)

#### Users
- `id` (Primary Key)
- `user_code` (Unique)
- `name`
- `email` (Unique)
- `phone`
- `address`
- `document`
- `status` (ACTIVE, INACTIVE, SUSPENDED)
- Timestamps (created_at, updated_at)

#### Loans
- `id` (Primary Key)
- `book_id` (Foreign Key)
- `user_id` (Foreign Key)
- `loan_date`
- `expected_return_date`
- `actual_return_date`
- `status` (ACTIVE, RETURNED, OVERDUE, RENEWED)
- `fine_amount`
- `observations`
- Timestamps (created_at, updated_at)

## 🧪 Testing

### Run All Tests
```bash
./mvnw test
```

### Run Specific Test Class
```bash
./mvnw test -Dtest=BookServiceTest
```

## 🔄 Git Flow Workflow

This project uses Git Flow for branch management:

### Main Branches
- `main` - Production-ready code
- `develop` - Integration branch for features

### Supporting Branches
- `feature/*` - New features
- `release/*` - Release preparation
- `hotfix/*` - Emergency fixes

### Common Commands
```bash
# Start new feature
git flow feature start feature-name

# Finish feature
git flow feature finish feature-name

# Start release
git flow release start v1.0.0

# Finish release
git flow release finish v1.0.0
```

## 📦 Sample Data

The application comes with pre-loaded sample data including:

### Books
- Clean Code: A Handbook of Agile Software Craftsmanship
- The Pragmatic Programmer
- Effective Java
- Head First Design Patterns
- Spring in Action

### Users
- John Smith (USR001)
- Mary Johnson (USR002)
- Robert Brown (USR003)
- Sarah Davis (USR004)
- Michael Wilson (USR005)

## 🚀 Deployment

### Docker (Coming Soon)
```dockerfile
FROM openjdk:17-jre-slim
COPY target/library-management-system-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Traditional Deployment
1. Build the JAR file:
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. Run the JAR:
   ```bash
   java -jar target/library-management-system-*.jar --spring.profiles.active=prod
   ```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git flow feature start amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📋 TODO

### High Priority
- [ ] Complete REST API implementation
- [ ] Implement web interface with Thymeleaf
- [ ] Add comprehensive validation
- [ ] Implement exception handling

### Medium Priority
- [ ] Add reservation system
- [ ] Implement email notifications
- [ ] Add file upload for book covers
- [ ] Create admin dashboard

### Low Priority
- [ ] Add unit and integration tests
- [ ] Implement user authentication
- [ ] Add API documentation with Swagger
- [ ] Create Docker containers
- [ ] Add internationalization (i18n)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

For support and questions:
- Create an [Issue](https://github.com/efojunior25/library-management-system/issues)
- Contact: [efojunior25@github.com](mailto:efojunior25@users.noreply.github.com)

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- Lombok for reducing boilerplate code
- Bootstrap for responsive UI components
- H2 Database for development convenience

---

**Made with ❤️ by Edson Junior**