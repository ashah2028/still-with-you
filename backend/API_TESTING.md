# Testing the Still With You API

## Starting the Server

### 1. Start PostgreSQL (first time setup)
```bash
cd backend
docker-compose up -d
```

### 2. Start Spring Boot
```bash
./mvnw spring-boot:run
```

The server will start at `http://localhost:8080`

## API Endpoints

### Patients

#### 1. Create a Patient
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "+1234567890",
    "hospitalName": "City Hospital",
    "roomNumber": "302"
  }'
```

Response:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John Doe",
  "phoneNumber": "+1234567890",
  "hospitalName": "City Hospital",
  "roomNumber": "302",
  "createdAt": "2025-01-10T12:00:00",
  "updatedAt": "2025-01-10T12:00:00"
}
```

#### 2. Get All Patients
```bash
curl http://localhost:8080/api/patients
```

#### 3. Get a Specific Patient
```bash
curl http://localhost:8080/api/patients/{patientId}
```

#### 4. Update a Patient
```bash
curl -X PUT http://localhost:8080/api/patients/{patientId} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "roomNumber": "303"
  }'
```

#### 5. Delete a Patient
```bash
curl -X DELETE http://localhost:8080/api/patients/{patientId}
```

### Messages

#### 1. Send a Message to a Patient
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": "123e4567-e89b-12d3-a456-426614174000",
    "senderName": "Mom",
    "content": "Hey honey! Hope you are feeling better. Love you!"
  }'
```

#### 2. Get All Messages for a Patient
```bash
curl http://localhost:8080/api/messages/patient/{patientId}
```

#### 3. Get a Specific Message
```bash
curl http://localhost:8080/api/messages/{messageId}
```

#### 4. Delete a Message
```bash
curl -X DELETE http://localhost:8080/api/messages/{messageId}
```

## Testing with VS Code REST Client

If you have the REST Client extension, create a file `test.http`:

```http
### Create Patient
POST http://localhost:8080/api/patients
Content-Type: application/json

{
  "name": "Jane Smith",
  "phoneNumber": "+19876543210",
  "hospitalName": "Mayo Clinic",
  "roomNumber": "405"
}

### Get All Patients
GET http://localhost:8080/api/patients

### Send Message
POST http://localhost:8080/api/messages
Content-Type: application/json

{
  "patientId": "PASTE_PATIENT_ID_HERE",
  "senderName": "Dad",
  "content": "Thinking of you! Get well soon!"
}

### Get Patient Messages
GET http://localhost:8080/api/messages/patient/PASTE_PATIENT_ID_HERE
```

## PostgreSQL Database Access

### Option 1: Command Line
```bash
docker exec -it stillwithyou-db psql -U postgres -d stillwithyou
```

Then run SQL:
```sql
\dt                     -- List tables
SELECT * FROM patients;
SELECT * FROM messages;
\q                      -- Quit
```

### Option 2: GUI Tools
Use TablePlus, pgAdmin, or DBeaver with these settings:
- Host: localhost
- Port: 5432
- Database: stillwithyou
- User: postgres
- Password: postgres

## Database Structure

**patients** table:
- id (UUID)
- name (VARCHAR)
- phone_number (VARCHAR, unique)
- hospital_name (VARCHAR)
- room_number (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

**messages** table:
- id (UUID)
- patient_id (UUID, foreign key)
- sender_name (VARCHAR)
- content (TEXT)
- sent_at (TIMESTAMP)
