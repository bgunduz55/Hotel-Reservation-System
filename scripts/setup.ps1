# Hotel Reservation System - Setup Script
# Windows PowerShell Setup Script

Write-Host "🏨 Hotel Reservation System - Setup Script" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green

# Change to project root directory
Write-Host "📁 Changing to project root directory..." -ForegroundColor Yellow
Set-Location -Path $PSScriptRoot\..
Write-Host "✅ Current directory: $(Get-Location)" -ForegroundColor Green

# Check if Docker is installed
Write-Host "🔍 Checking Docker installation..." -ForegroundColor Yellow
try {
    $dockerVersion = docker --version
    Write-Host "✅ Docker is installed: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Docker Desktop from: https://www.docker.com/products/docker-desktop" -ForegroundColor Yellow
    exit 1
}

# Check if Docker Compose is available
Write-Host "🔍 Checking Docker Compose..." -ForegroundColor Yellow
try {
    $composeVersion = docker-compose --version
    Write-Host "✅ Docker Compose is available: $composeVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker Compose is not available" -ForegroundColor Red
    Write-Host "Please ensure Docker Compose is installed" -ForegroundColor Yellow
    exit 1
}

# Build Docker images
Write-Host "🐳 Building Docker images..." -ForegroundColor Yellow
Write-Host "This may take several minutes. Please wait..." -ForegroundColor Yellow
try {
    docker-compose build --no-cache
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Docker images built successfully" -ForegroundColor Green
    } else {
        Write-Host "❌ Failed to build Docker images" -ForegroundColor Red
        Write-Host "Please check the Docker output for errors" -ForegroundColor Yellow
        exit 1
    }
} catch {
    Write-Host "❌ Failed to build Docker images" -ForegroundColor Red
    Write-Host "Please check the Docker output for errors" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "🎉 Setup completed successfully!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Run: .\scripts\start.ps1" -ForegroundColor Cyan
Write-Host "2. Access API Gateway: http://localhost:8080" -ForegroundColor Cyan
Write-Host "3. Access Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host "4. Access Eureka: http://localhost:8761" -ForegroundColor Cyan
Write-Host ""
Write-Host "For more information, see README.md" -ForegroundColor Blue
