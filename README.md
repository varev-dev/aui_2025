# HPC Cluster Manager (AUI 2025)

![CI Pipeline](https://github.com/varev-dev/aui_2025/actions/workflows/ci-pipeline.yml/badge.svg)

A microservices-based application for managing High Performance Computing (HPC) clusters and nodes. The system allows users to create, view, and manage computing resources through a modern web interface.

## 🛠️ Tech Stack

**Backend:**
* **Java 21** & **Spring Boot 3**
* **Spring Cloud Gateway** (API Gateway)
* **Spring Data JPA** (Hibernate)
* **H2** (Dev) / **PostgreSQL** (Prod)

**Frontend:**
* **Angular 18+** (Standalone Components)
* **Nginx** (Reverse Proxy & Static Server)
* **Bootstrap / Tailwind** (jeśli używasz CSS frameworka)

**DevOps & Infrastructure:**
* **Docker** & **Docker Compose**
* **GitHub Actions** (CI Pipeline)
* **Maven** & **NPM**

## Project Deployment

### Database Configuration Modes

| Variant | Compose File | Database | Description |
| :--- | :--- | :--- | :--- |
| **Default (Dev)** | `docker-compose.yml` | **H2 (In-Memory)** | Fast startup. Data is **lost** when containers stop. Ideal for development and quick testing. |
| **Persistent** | `docker-compose-postgres.yml` | **PostgreSQL** | Full database setup. Data is **persisted** on a volume after restarts. Simulates a production environment. |

### Build and Run

#### Default variant (H2)

```bash
docker-compose up -d --build
```

#### Persistent variant (PostgreSQL)
```bash
docker-compose -f docker-compose-postgres.yml up -d --build
```

### Accessing the Application

Once the containers are up and running, the system is accessible at:

- **Frontend (Web App):** http://localhost
- **API Gateway:** http://localhost:8080

### Stopping the Application

To stop and remove the containers:

#### For H2 version:
```bash
docker-compose down
```

#### For PostgreSQL version:
```bash
docker-compose -f docker-compose-postgres.yml down
```
