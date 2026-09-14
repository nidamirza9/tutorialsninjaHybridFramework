# Tutorials Ninja — Hybrid Automation Framework

[![CI — Selenium Grid + Maven](https://github.com/nidamirza9/tutorialsninjaHybridFramework/actions/workflows/ci.yml/badge.svg)](https://github.com/nidamirza9/tutorialsninjaHybridFramework/actions/workflows/ci.yml)

Java · Selenium WebDriver · TestNG · Maven · Page Object Model · Extent Reports · **Jenkins CI/CD** · Docker Selenium Grid

## What this repo demonstrates

- Hybrid TestNG automation framework (POM + data-driven tests)
- **Containerized CI/CD**: every push can run the suite against **Selenium Grid** and publish **Extent Reports**
- Local **Jenkins** stack (Docker Compose) for an owned pipeline — not just “used Jenkins once”

## Quick start (local tests)

```bash
mvn clean test -DsuiteXmlFile=src/test/java/testBase/grouping.xml
```

## CI / CD

### Option A — Jenkins (Docker Compose) — recommended for demos & interviews

Prerequisites: [Docker Desktop](https://www.docker.com/products/docker-desktop/) running.

```bash
docker compose -f docker-compose.jenkins.yml up -d --build
```

- Jenkins UI: http://localhost:8080  
- Login: `admin` / `admin123`  
- Job (auto-created via JCasC): **tutorialsninja-hybrid-ci**  
- Selenium Grid: http://localhost:4444  

Open the job → **Build Now**. The pipeline:

1. Checks out the repo  
2. Confirms Selenium Grid is up  
3. Builds the Maven test image  
4. Runs `ci-suite.xml` against Grid (Chrome, headless)  
5. Archives / publishes Extent HTML reports  

Stop stack:

```bash
docker compose -f docker-compose.jenkins.yml down
```

### Option B — GitHub Actions (cloud CI badge)

Push to `main`/`master` triggers `.github/workflows/ci.yml` (Grid + Maven + report artifacts).

### Option C — Grid only + Maven on host

```bash
docker compose -f docker-compose.grid.yml up -d
mvn -B -Pci test -Dexecution_env=remote -Dselenium.grid.url=http://localhost:4444/wd/hub -Dheadless=true
docker compose -f docker-compose.grid.yml down
```

## Pipeline files

| File | Purpose |
|------|---------|
| `Jenkinsfile` | Declarative Jenkins pipeline |
| `docker-compose.jenkins.yml` | Jenkins + Hub + Chrome node |
| `docker-compose.grid.yml` | Selenium Grid only |
| `Dockerfile` | Maven test runner image |
| `Dockerfile.jenkins` | Jenkins LTS + Docker CLI + plugins |
| `src/test/java/testBase/ci-suite.xml` | CI smoke suite |

## Resume-ready bullet

> Containerized and automated the regression suite via a Jenkins CI/CD pipeline (Docker + Selenium Grid), triggering full test execution on commit and publishing Extent Reports as build artifacts — eliminating manual pipeline triggers.

## Notes

- Override runtime: `EXECUTION_ENV`, `SELENIUM_REMOTE_URL`, `HEADLESS`
- App under test: https://tutorialsninja.com/demo/
