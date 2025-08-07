# 🏨 Hotel Reservation System

A microservices-based hotel reservation system built with Spring Boot, Docker, and Apache Kafka.

## 🚀 Quick Start

### Prerequisites
- Docker Desktop
- Docker Compose

### Setup and Run

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Hotel-Reservation-System
   ```

2. **Run setup script (Windows)**
   ```powershell
   .\scripts\setup.ps1
   ```

3. **Start all services**
   ```powershell
   .\scripts\start.ps1
   ```

4. **Stop services**
   ```bash
   docker-compose down
   ```

## 📋 Available Scripts

### Windows PowerShell Scripts

- **`setup.ps1`** - Initial setup and Docker image building
- **`start.ps1`** - Start all services

### Usage
```powershell
# Setup (first time only)
.\scripts\setup.ps1

# Start services
.\scripts\start.ps1

# Stop services
docker-compose down

# View logs
docker-compose logs -f
```

## 🌐 Service URLs

Once running, access the services at:

- **API Gateway**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Hotel Service**: http://localhost:8081
- **Reservation Service**: http://localhost:8082
- **Notification Service**: http://localhost:8083
- **Discovery Service (Eureka)**: http://localhost:8761

## 🔐 Demo Credentials

- **Admin**: username=admin, password=admin123
- **User**: username=user, password=user123
- **Guest**: username=guest, password=guest123

## 🏗️ Architecture

### Microservices
- **API Gateway** (Port: 8080) - Spring Cloud Gateway
- **Hotel Service** (Port: 8081) - Hotel and room management
- **Reservation Service** (Port: 8082) - Reservation management
- **Notification Service** (Port: 8083) - Email notifications
- **Discovery Service** (Port: 8761) - Eureka Server

### Infrastructure
- **PostgreSQL** - Database
- **Apache Kafka** - Message broker
- **Zookeeper** - Kafka coordination

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x, Spring Data JPA, Spring Security
- **Database**: PostgreSQL
- **Message Broker**: Apache Kafka
- **Containerization**: Docker & Docker Compose
- **Authentication**: JWT
- **Documentation**: OpenAPI 3 (Swagger)

## 📁 Project Structure

```
Hotel-Reservation-System/
├── api-gateway/          # API Gateway service
├── hotel-service/        # Hotel management service
├── reservation-service/  # Reservation management service
├── notification-service/ # Notification service
├── discovery-service/    # Eureka discovery service
├── scripts/             # PowerShell scripts
├── postman/             # API testing collection
├── db/                  # Database initialization
├── docker-compose.yml   # Docker orchestration
└── pom.xml             # Parent Maven POM
```

## 🔧 Development

### Building Services
```bash
# Build all services
mvn clean install -DskipTests

# Build specific service
mvn clean install -pl hotel-service -am
```

### Running Locally
```bash
# Start infrastructure only
docker-compose up postgres kafka zookeeper

# Run services individually
mvn spring-boot:run -pl hotel-service
mvn spring-boot:run -pl reservation-service
```

## 📊 API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Postman Collection**: `postman/Hotel-Reservation-System.postman_collection.json`

## 🧪 Testing

### API Testing
Import the Postman collection for comprehensive API testing.

### Unit Testing
```bash
# Run all tests
mvn test

# Run specific service tests
mvn test -pl hotel-service
```

## 🐛 Troubleshooting

### Common Issues

1. **Docker not running**
   - Start Docker Desktop
   - Ensure Docker is running

2. **Port conflicts**
   - Check if ports 8080-8083, 8761, 5432, 9092 are available
   - Stop conflicting services

3. **Database connection issues**
   - Ensure PostgreSQL container is healthy
   - Check database credentials in application.yml

4. **Kafka connection issues**
   - Ensure Zookeeper and Kafka containers are healthy
   - Check Kafka configuration

### Logs
```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f hotel-service
docker-compose logs -f reservation-service
```

## 📝 License

This project is for educational purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request 