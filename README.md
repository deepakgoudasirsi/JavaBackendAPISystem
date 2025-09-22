# Java Backend API System

A comprehensive Java backend system built with Spring Boot for scalable web application development. This project demonstrates modern Java development practices with RESTful APIs, JWT authentication, database integration, and comprehensive testing.

## 🚀 Features

- **RESTful API Design**: Clean, well-structured REST endpoints
- **JWT Authentication**: Secure token-based authentication and authorization
- **Database Integration**: MySQL database with JPA/Hibernate ORM
- **Comprehensive Testing**: Unit tests with JUnit and Mockito
- **API Documentation**: Swagger/OpenAPI documentation
- **Exception Handling**: Global exception handling with proper error responses
- **Logging & Monitoring**: Structured logging and health monitoring
- **Security**: Role-based access control with Spring Security
- **Validation**: Input validation with Bean Validation

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Primary database
- **H2** - In-memory database for testing
- **JWT** - JSON Web Tokens for authentication
- **Swagger/OpenAPI** - API documentation
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework
- **Maven** - Build and dependency management
- **Lombok** - Code generation

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Git

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd java-backend-api-system
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE backend_api_db;
CREATE USER 'api_user'@'localhost' IDENTIFIED BY 'api_password';
GRANT ALL PRIVILEGES ON backend_api_db.* TO 'api_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configuration

Update the database configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/backend_api_db
    username: api_user
    password: api_password
```

### 4. Build and Run

```bash
# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 5. Access API Documentation

Once the application is running, access the Swagger UI at:
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs

## 📚 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register new user
- `POST /api/auth/signin` - User login
- `GET /api/auth/me` - Get current user profile

### User Management
- `GET /api/users` - Get all users (Admin only)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user (Admin only)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user (Admin only)
- `GET /api/users/search?name={name}` - Search users by name

### Post Management
- `GET /api/posts` - Get published posts
- `GET /api/posts/{id}` - Get post by ID
- `POST /api/posts` - Create new post
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post
- `GET /api/posts/search?searchTerm={term}` - Search posts
- `GET /api/posts/my-posts` - Get current user's posts

### Comment Management
- `GET /api/comments` - Get all comments (Admin only)
- `GET /api/comments/{id}` - Get comment by ID
- `POST /api/comments/post/{postId}` - Create comment on post
- `PUT /api/comments/{id}` - Update comment
- `DELETE /api/comments/{id}` - Delete comment
- `GET /api/comments/post/{postId}` - Get comments for post

## 🔐 Authentication

The API uses JWT (JSON Web Token) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### User Roles
- **USER**: Basic user with limited permissions
- **ADMIN**: Administrative user with full access
- **MODERATOR**: User with moderation capabilities

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=AuthServiceTest
```

### Run Tests with Coverage
```bash
mvn test jacoco:report
```

## 📊 Monitoring

### Health Check
- **Health Endpoint**: http://localhost:8080/api/actuator/health
- **Info Endpoint**: http://localhost:8080/api/actuator/info
- **Metrics**: http://localhost:8080/api/actuator/metrics

### Logs
Application logs are written to:
- Console output
- `logs/application.log` file

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/backendapi/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Data repositories
│   │   ├── security/       # Security configuration
│   │   └── service/        # Business logic
│   └── resources/
│       ├── application.yml # Application configuration
│       └── logs/          # Log files
└── test/
    ├── java/com/backendapi/
    │   ├── controller/     # Controller tests
    │   └── service/        # Service tests
    └── resources/
        └── application-test.yml # Test configuration
```

## 🔧 Configuration

### Environment Variables
- `SPRING_PROFILES_ACTIVE` - Active Spring profile
- `DB_HOST` - Database host
- `DB_PORT` - Database port
- `DB_NAME` - Database name
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key
- `JWT_EXPIRATION` - JWT expiration time

### Profiles
- **default**: Development profile
- **test**: Testing profile with H2 database
- **prod**: Production profile

## 🚀 Deployment

### Docker Deployment
```bash
# Build Docker image
docker build -t java-backend-api .

# Run container
docker run -p 8080:8080 java-backend-api
```

### Production Deployment
1. Set production profile: `SPRING_PROFILES_ACTIVE=prod`
2. Configure production database
3. Set secure JWT secret
4. Enable HTTPS
5. Configure logging levels

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact: support@backendapi.com

## 🎯 Future Enhancements

- [ ] Redis caching
- [ ] Message queuing with RabbitMQ
- [ ] Microservices architecture
- [ ] GraphQL API
- [ ] Real-time notifications
- [ ] File upload functionality
- [ ] Email notifications
- [ ] Advanced search with Elasticsearch
