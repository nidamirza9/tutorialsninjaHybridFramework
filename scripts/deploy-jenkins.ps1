# Deploy local Jenkins + Selenium Grid CI stack (Windows PowerShell)
# Prerequisite: Docker Desktop installed and running
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot\..

Write-Host "Building and starting Jenkins + Selenium Grid..." -ForegroundColor Cyan
docker compose -f docker-compose.jenkins.yml up -d --build

Write-Host ""
Write-Host "Jenkins UI : http://localhost:8080" -ForegroundColor Green
Write-Host "Login      : admin / admin123" -ForegroundColor Green
Write-Host "Grid       : http://localhost:4444" -ForegroundColor Green
Write-Host "Job        : tutorialsninja-hybrid-ci (Build Now after plugins finish loading)" -ForegroundColor Green
Write-Host ""
Write-Host "First boot can take 2-5 minutes while Jenkins installs plugins." -ForegroundColor Yellow
