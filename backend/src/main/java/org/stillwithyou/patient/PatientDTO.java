package org.stillwithyou.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Objects (DTOs) - used for API requests/responses
 *
 * WHY USE DTOs instead of exposing the Entity directly?
 *
 * 1. SECURITY: Hide internal database structure
 *    - Don't expose database IDs that could be guessed
 *    - Don't return sensitive fields (password hashes, etc.)
 *
 * 2. API STABILITY: Change database without breaking API
 *    - Rename database columns without affecting frontend
 *    - Add database fields without exposing them
 *
 * 3. VALIDATION: Different validation for create vs update
 *    - Creating a patient: email is required
 *    - Updating a patient: email might be optional
 *
 * 4. CLEAN API: Return only what frontend needs
 *    - Don't return createdAt/updatedAt if frontend doesn't use them
 *    - Don't return empty fields
 */

/**
 * Request DTO - used when creating a new patient
 * Frontend sends this in POST /api/patients
 */
class CreatePatientRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    private String hospitalName;  // Optional
    private String roomNumber;    // Optional

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    /**
     * Convert DTO to Entity
     * Used in the service layer
     */
    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setName(this.name);
        patient.setEmail(this.email);
        patient.setHospitalName(this.hospitalName);
        patient.setRoomNumber(this.roomNumber);
        return patient;
    }
}

/**
 * Request DTO - used when updating a patient
 * Frontend sends this in PUT /api/patients/{id}
 *
 * Note: All fields are optional for partial updates
 */
class UpdatePatientRequest {
    private String name;
    private String hospitalName;
    private String roomNumber;
    // Email is NOT here - we don't allow email changes after registration

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
}

/**
 * Response DTO - returned from all GET endpoints
 * This is what the frontend receives
 */
class PatientResponse {
    private UUID id;
    private String name;
    private String email;
    private String hospitalName;
    private String roomNumber;
    private LocalDateTime createdAt;

    // Constructor to create from Entity
    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.email = patient.getEmail();
        this.hospitalName = patient.getHospitalName();
        this.roomNumber = patient.getRoomNumber();
        this.createdAt = patient.getCreatedAt();
        // Note: We're NOT returning updatedAt - frontend doesn't need it
    }

    // Getters only (immutable response)
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getHospitalName() { return hospitalName; }
    public String getRoomNumber() { return roomNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

/**
 * USAGE IN CONTROLLER (updated version):
 *
 * @PostMapping
 * public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
 *     Patient patient = request.toEntity();
 *     Patient created = patientService.createPatient(patient);
 *     return ResponseEntity.status(201).body(new PatientResponse(created));
 * }
 *
 * @GetMapping("/{id}")
 * public ResponseEntity<PatientResponse> getPatient(@PathVariable UUID id) {
 *     Patient patient = patientService.getPatient(id);
 *     return ResponseEntity.ok(new PatientResponse(patient));
 * }
 *
 * @PutMapping("/{id}")
 * public ResponseEntity<PatientResponse> updatePatient(
 *         @PathVariable UUID id,
 *         @RequestBody UpdatePatientRequest request) {
 *     Patient updates = new Patient();
 *     updates.setName(request.getName());
 *     updates.setHospitalName(request.getHospitalName());
 *     updates.setRoomNumber(request.getRoomNumber());
 *
 *     Patient updated = patientService.updatePatient(id, updates);
 *     return ResponseEntity.ok(new PatientResponse(updated));
 * }
 */

/**
 * ALTERNATIVE: Use Lombok to reduce boilerplate
 *
 * Add dependency to pom.xml:
 * <dependency>
 *     <groupId>org.projectlombok</groupId>
 *     <artifactId>lombok</artifactId>
 * </dependency>
 *
 * Then your DTOs become:
 *
 * @Data  // Generates getters, setters, toString, equals, hashCode
 * class CreatePatientRequest {
 *     @NotBlank
 *     private String name;
 *     @Email @NotBlank
 *     private String email;
 *     private String hospitalName;
 *     private String roomNumber;
 * }
 *
 * Much cleaner!
 */
