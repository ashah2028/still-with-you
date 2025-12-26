package org.stillwithyou.patient;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Patient Controller - handles HTTP requests for patient operations
 *
 * This is the "Presentation Layer" (or API Layer) - it:
 * 1. Receives HTTP requests from the frontend
 * 2. Validates request data
 * 3. Calls the service layer to do the work
 * 4. Returns HTTP responses
 *
 * This controller exposes a RESTful API at /api/patients
 */
@RestController  // @Controller + @ResponseBody - returns JSON instead of HTML
@RequestMapping("/api/patients")  // All endpoints start with /api/patients
public class PatientController {

    // Inject the service layer
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * CREATE - Register a new patient
     *
     * HTTP: POST /api/patients
     * Body: { "name": "John", "email": "john@example.com", ... }
     * Response: 201 Created with the patient object
     *
     * @Valid triggers validation annotations in Patient entity
     * @RequestBody converts JSON to Patient object automatically
     */
    @PostMapping  // Handles POST requests to /api/patients
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        Patient created = patientService.createPatient(patient);

        // ResponseEntity lets you control:
        // - HTTP status code (201 Created)
        // - Response body (the patient object)
        // - Headers (if needed)
        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201
                .body(created);
    }

    /**
     * READ - Get a single patient by ID
     *
     * HTTP: GET /api/patients/{id}
     * Example: GET /api/patients/123e4567-e89b-12d3-a456-426614174000
     * Response: 200 OK with patient object
     *
     * @PathVariable extracts {id} from the URL
     * Spring automatically converts the string to UUID
     */
    @GetMapping("/{id}")  // Handles GET requests to /api/patients/{id}
    public ResponseEntity<Patient> getPatient(@PathVariable UUID id) {
        Patient patient = patientService.getPatient(id);
        return ResponseEntity.ok(patient);  // 200 OK
    }

    /**
     * READ - Get all patients
     *
     * HTTP: GET /api/patients
     * Response: 200 OK with array of patients
     *
     * In production, you'd add:
     * - Pagination: ?page=0&size=20
     * - Filtering: ?hospitalName=Mayo
     * - Sorting: ?sort=name,asc
     */
    @GetMapping  // Handles GET requests to /api/patients
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);  // 200 OK
    }

    /**
     * UPDATE - Update patient information
     *
     * HTTP: PUT /api/patients/{id}
     * Body: { "name": "Updated Name", ... }
     * Response: 200 OK with updated patient
     *
     * Note: This is a partial update (only updates provided fields)
     * For full replacement, you'd validate all required fields
     */
    @PutMapping("/{id}")  // Handles PUT requests to /api/patients/{id}
    public ResponseEntity<Patient> updatePatient(
            @PathVariable UUID id,
            @RequestBody Patient updates) {
        Patient updated = patientService.updatePatient(id, updates);
        return ResponseEntity.ok(updated);  // 200 OK
    }

    /**
     * DELETE - Remove a patient
     *
     * HTTP: DELETE /api/patients/{id}
     * Response: 204 No Content (success, no body needed)
     */
    @DeleteMapping("/{id}")  // Handles DELETE requests to /api/patients/{id}
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    /**
     * HOW THIS MAPS TO HTTP:
     *
     * Frontend makes request:
     *   POST http://localhost:8080/api/patients
     *   Content-Type: application/json
     *   { "name": "John", "email": "john@example.com" }
     *
     * Spring does:
     *   1. Routes request to createPatient() method
     *   2. Deserializes JSON → Patient object
     *   3. Validates with @Valid (checks @NotBlank, @Email)
     *   4. Calls method
     *   5. Serializes returned Patient → JSON
     *   6. Sends HTTP response
     *
     * Response:
     *   HTTP/1.1 201 Created
     *   Content-Type: application/json
     *   {
     *     "id": "123e4567-e89b-12d3-a456-426614174000",
     *     "name": "John",
     *     "email": "john@example.com",
     *     "createdAt": "2025-12-26T01:00:00",
     *     ...
     *   }
     */

    /**
     * ERROR HANDLING:
     *
     * What if patient not found? Service throws IllegalArgumentException.
     * By default, Spring returns 500 Internal Server Error.
     *
     * To return proper 404 Not Found, you'd add a global exception handler:
     *
     * @ControllerAdvice
     * public class GlobalExceptionHandler {
     *     @ExceptionHandler(IllegalArgumentException.class)
     *     public ResponseEntity<?> handleNotFound(IllegalArgumentException ex) {
     *         return ResponseEntity.status(404).body(ex.getMessage());
     *     }
     * }
     *
     * We'll create this in the common/ package later.
     */
}
