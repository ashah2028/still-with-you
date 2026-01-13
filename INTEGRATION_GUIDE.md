# Frontend ↔ Backend Integration Guide

## Architecture

```
Frontend (React)          Backend (Spring Boot)       Database (PostgreSQL)
Port 5173                 Port 8080                   Port 5432
     │                         │                           │
     │──── HTTP Requests ─────>│                           │
     │     (fetch API)          │──── SQL Queries ────────>│
     │<──── JSON Response ──────│<──── Query Results ──────│
```

## What We Built

### Backend (backend/)
- **CorsConfig.java** - Allows frontend to make requests
- **Patient API** - `/api/patients` endpoints
- **Message API** - `/api/messages` endpoints
- **PostgreSQL** - Persistent data storage

### Frontend (frontend/)
- **types.ts** - TypeScript interfaces matching backend models
- **services/api.ts** - API service layer (patientApi, messageApi)
- **components/**
  - PatientForm - Register new patients
  - PatientList - Display all patients
  - MessageForm - Send messages to patients
  - MessageList - Display patient messages
- **App.tsx** - Main app orchestrating everything

## How to Run

### Terminal 1: Start PostgreSQL
```bash
cd backend
docker-compose up -d
```

### Terminal 2: Start Backend
```bash
cd backend
./mvnw spring-boot:run
```

Wait for: `Started BackendApplication in X seconds`

### Terminal 3: Start Frontend
```bash
cd frontend
npm install  # First time only
npm run dev
```

Open: `http://localhost:5173`

## How It Works

### 1. Frontend Makes Request
```typescript
// In frontend/src/services/api.ts
const response = await fetch('http://localhost:8080/api/patients', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ name: 'John', phoneNumber: '+1234567890' })
});
```

### 2. Backend Receives Request
```java
// In PatientController.java
@PostMapping
public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
    Patient created = patientService.createPatient(patient);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

### 3. Backend Saves to Database
```java
// In PatientService.java
public Patient createPatient(Patient patient) {
    return patientRepository.save(patient);  // SQL INSERT
}
```

### 4. Backend Returns JSON
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John",
  "phoneNumber": "+1234567890",
  "createdAt": "2025-01-11T18:00:00"
}
```

### 5. Frontend Receives Response
```typescript
// In App.tsx
const patient = await patientApi.create({ name, phoneNumber });
setPatients([...patients, patient]);  // Update UI
```

## Testing the Integration

### 1. Register a Patient
- Open `http://localhost:5173`
- Fill in the "Register Patient" form
- Click "Register Patient"
- Patient appears in the list below

### 2. Send a Message
- Click on a patient in the list
- Fill in "Send Message" form on the right
- Click "Send Message"
- Message appears in the list

### 3. Check the Database
```bash
docker exec -it stillwithyou-db psql -U postgres -d stillwithyou

SELECT * FROM patients;
SELECT * FROM messages;
```

## Common Issues

### "Failed to load patients"
- Backend not running → Start with `./mvnw spring-boot:run`
- Check Terminal 2 for errors

### CORS errors in browser console
- Check `CorsConfig.java` has `allowedOrigins("http://localhost:5173")`
- Restart backend after changes

### Database connection error
- PostgreSQL not running → `docker-compose up -d`
- Check with `docker ps`

### Port already in use
- Frontend (5173): Kill other Vite processes
- Backend (8080): Kill other Spring Boot processes
- Database (5432): `docker-compose down` then `up -d`

## Project Structure

```
still-with-you/
├── backend/
│   ├── src/main/java/org/stillwithyou/
│   │   ├── config/
│   │   │   └── CorsConfig.java          ← Enables frontend requests
│   │   ├── patient/
│   │   │   ├── Patient.java
│   │   │   ├── PatientController.java   ← REST endpoints
│   │   │   ├── PatientService.java
│   │   │   └── PatientRepository.java
│   │   └── message/
│   │       ├── Message.java
│   │       ├── MessageController.java
│   │       ├── MessageService.java
│   │       └── MessageRepository.java
│   ├── docker-compose.yml               ← PostgreSQL setup
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── components/
    │   │   ├── PatientForm.tsx          ← Register patients
    │   │   ├── PatientList.tsx          ← Display patients
    │   │   ├── MessageForm.tsx          ← Send messages
    │   │   └── MessageList.tsx          ← Display messages
    │   ├── services/
    │   │   └── api.ts                   ← API calls to backend
    │   ├── types.ts                     ← TypeScript interfaces
    │   └── App.tsx                      ← Main component
    └── package.json
```

## Next Steps

1. **Add styling** - Use Tailwind CSS or MUI
2. **Add routing** - Use React Router for multiple pages
3. **Add authentication** - Protect API endpoints
4. **Add real-time updates** - Use WebSockets for live messages
5. **Deploy** - Host on Vercel (frontend) + Railway/Heroku (backend)
