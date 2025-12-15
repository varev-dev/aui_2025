# AUI 2025

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
