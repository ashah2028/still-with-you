# PostgreSQL Setup Guide

## Quick Start with Docker

### 1. Start PostgreSQL
```bash
cd backend
docker-compose up -d
```

This will:
- Download PostgreSQL 15 (first time only)
- Create a database named `stillwithyou`
- Start it on port 5432
- Run in the background (`-d` = detached mode)

### 2. Verify it's running
```bash
docker ps
```

You should see `stillwithyou-db` in the list.

### 3. Start your Spring Boot app
```bash
./mvnw spring-boot:run
```

The app will automatically:
- Connect to PostgreSQL
- Create the `patients` and `messages` tables
- Be ready to accept requests!

## Database Connection Info

- **Host:** localhost
- **Port:** 5432
- **Database:** stillwithyou
- **Username:** postgres
- **Password:** postgres

## Useful Docker Commands

### Stop the database
```bash
docker-compose down
```

### Stop and delete all data (fresh start)
```bash
docker-compose down -v
```

### View database logs
```bash
docker logs stillwithyou-db
```

### Connect to PostgreSQL via command line
```bash
docker exec -it stillwithyou-db psql -U postgres -d stillwithyou
```

Then you can run SQL:
```sql
\dt                          -- List all tables
SELECT * FROM patients;      -- View patients
SELECT * FROM messages;      -- View messages
\q                          -- Quit
```

## GUI Tools (Optional)

You can use these tools to view your database with a nice interface:

1. **pgAdmin** - Free, feature-rich
   - Download: https://www.pgadmin.org/download/

2. **TablePlus** - Clean, modern UI
   - Download: https://tableplus.com/

3. **DBeaver** - Free, cross-platform
   - Download: https://dbeaver.io/

Connection settings for all tools:
- Host: localhost
- Port: 5432
- Database: stillwithyou
- User: postgres
- Password: postgres

## Troubleshooting

### Port 5432 already in use
If you have PostgreSQL installed locally:
```bash
# Stop local PostgreSQL (macOS)
brew services stop postgresql
```

Or change the port in `docker-compose.yml`:
```yaml
ports:
  - "5433:5432"  # Use 5433 instead
```

Then update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/stillwithyou
```

### Can't connect to database
1. Make sure Docker is running
2. Check database is up: `docker ps`
3. Check logs: `docker logs stillwithyou-db`

## Data Persistence

Your data is saved in a Docker volume called `postgres-data`. This means:
- ✅ Data survives when you stop the container
- ✅ Data survives when you restart your computer
- ❌ Data is deleted if you run `docker-compose down -v`
