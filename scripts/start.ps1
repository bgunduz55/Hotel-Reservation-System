# Hotel Reservation System - Start Script
# Windows PowerShell Start Script

Write-Host "Hotel Reservation System - Start Script" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green

# Change to project root directory
Write-Host "Changing to project root directory..." -ForegroundColor Yellow
Set-Location -Path $PSScriptRoot\..
Write-Host "Current directory: $(Get-Location)" -ForegroundColor Green

# Check if Docker is running
Write-Host "Checking if Docker is running..." -ForegroundColor Yellow
try {
    docker info | Out-Null
    Write-Host "Docker is running" -ForegroundColor Green
} catch {
    Write-Host "Docker is not running" -ForegroundColor Red
    Write-Host "Please start Docker Desktop and try again" -ForegroundColor Yellow
    exit 1
}

# Stop any existing containers
Write-Host "Stopping any existing containers..." -ForegroundColor Yellow
try {
    docker-compose down
    Write-Host "Existing containers stopped" -ForegroundColor Green
} catch {
    Write-Host "No existing containers to stop" -ForegroundColor Blue
}

# Start all services
Write-Host "Starting all services..." -ForegroundColor Yellow
Write-Host "This may take a few minutes. Please wait..." -ForegroundColor Yellow
try {
    docker-compose up -d
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ All services started successfully" -ForegroundColor Green
    } else {
        Write-Host "❌ Failed to start services" -ForegroundColor Red
        Write-Host "Please check the Docker Compose output for errors" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "❌ Failed to start services" -ForegroundColor Red
    Write-Host "Please check the Docker Compose output for errors" -ForegroundColor Yellow
    exit 1
}

# Wait for services to be ready
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Write-Host "This may take a few minutes..." -ForegroundColor Yellow
Start-Sleep -Seconds 60

# Check service health
Write-Host "Checking service health..." -ForegroundColor Yellow
try {
    $healthyServices = docker-compose ps --format "table {{.Name}}\t{{.Status}}" | Select-String "healthy"
    if ($healthyServices) {
        Write-Host "✅ Services are healthy" -ForegroundColor Green
    } else {
        Write-Host "⚠️ Some services may still be starting up" -ForegroundColor Yellow
    }
} catch {
    Write-Host "⚠️ Could not check service health" -ForegroundColor Yellow
}

# Show running containers
Write-Host ""
Write-Host "Running containers:" -ForegroundColor Yellow
try {
    docker-compose ps
} catch {
    Write-Host "Failed to get container status" -ForegroundColor Red
}

# Show service URLs
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Yellow
Write-Host "Frontend: http://localhost:3000" -ForegroundColor Cyan
Write-Host "API Gateway: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "Hotel Service: http://localhost:8081" -ForegroundColor Cyan
Write-Host "Reservation Service: http://localhost:8082" -ForegroundColor Cyan
Write-Host "Notification Service: http://localhost:8083" -ForegroundColor Cyan
Write-Host "Discovery Service: http://localhost:8761" -ForegroundColor Cyan

# Show demo credentials
Write-Host ""
Write-Host "Demo Credentials:" -ForegroundColor Yellow
Write-Host "Admin: username=admin, password=admin123" -ForegroundColor Cyan
Write-Host "User: username=user, password=user123" -ForegroundColor Cyan
Write-Host "Guest: username=guest, password=guest123" -ForegroundColor Cyan

Write-Host ""
Write-Host "Hotel Reservation System is now running!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host "To stop the services, run: docker-compose down" -ForegroundColor Yellow
Write-Host "To view logs, run: docker-compose logs -f" -ForegroundColor Yellow
