package org.stillwithyou.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Patient Service - contains business logic for patient operations
 *
 * This is the "Business Logic Layer" - it sits between the Controller and Repository.
 *
 * WHY NOT put logic directly in the Controller?
 * - Controllers should be thin (just handle HTTP)
 * - Services contain reusable business logic
 * - Services can be used by multiple controllers, scheduled jobs, etc.
 * - Easier to test (no HTTP mocking needed)
 *
 * WHY NOT put logic directly in the Repository?
 * - Repositories only do database operations
 * - Services handle validation, orchestration, transactions
 */
@Service  // Marks this as a Spring bean for business logic
@Transactional  // All methods run in database transactions (explained below)
public class PatientService {

    /**
     * Dependency Injection - Spring automatically provides the repository
     * Using constructor injection (preferred over @Autowired field injection)
     */
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * CREATE - Register a new patient
     *
     * Business logic:
     * 1. Validate email is unique (business rule)
     * 2. Save to database
     *
     * @throws IllegalArgumentException if email already exists
     */
    public Patient createPatient(Patient patient) {
        // Business validation
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Patient with email already exists");
        }

        // Save to database
        // The repository.save() will:
        // - Trigger @PrePersist hook (sets createdAt, updatedAt)
        // - Generate UUID
        // - Execute INSERT statement
        // - Return the saved entity with generated ID
        return patientRepository.save(patient);
    }

    /**
     * READ - Get patient by ID
     *
     * @Transactional(readOnly = true) optimizes read-only operations:
     * - Database can skip transaction log writes
     * - Hibernate skips dirty checking
     * - Small performance improvement
     *
     * @throws IllegalArgumentException if patient not found
     */
    @Transactional(readOnly = true)
    public Patient getPatient(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        // .orElseThrow() unwraps the Optional<Patient>
        // If patient exists → return patient
        // If patient doesn't exist → throw exception
    }

    /**
     * READ - Get patient by email (for authentication)
     */
    @Transactional(readOnly = true)
    public Patient getPatientByEmail(String email) {
        return patientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    /**
     * READ - Get all patients
     */
    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * UPDATE - Update patient information
     *
     * Pattern: Load entity → Modify fields → Save
     * JPA detects changes and generates UPDATE SQL automatically
     *
     * Note: We only update fields that are provided (partial update)
     * Email cannot be updated (not in this method)
     */
    public Patient updatePatient(UUID id, Patient updates) {
        // Load existing patient
        Patient patient = getPatient(id);

        // Apply updates (only if provided)
        if (updates.getName() != null) {
            patient.setName(updates.getName());
        }
        if (updates.getHospitalName() != null) {
            patient.setHospitalName(updates.getHospitalName());
        }
        if (updates.getRoomNumber() != null) {
            patient.setRoomNumber(updates.getRoomNumber());
        }

        // Save changes
        // @PreUpdate hook will update the updatedAt timestamp
        // JPA will generate: UPDATE patients SET name=?, hospital_name=?, updated_at=? WHERE id=?
        return patientRepository.save(patient);
    }

    /**
     * DELETE - Remove patient
     *
     * In production, you might want:
     * - Soft delete (mark as deleted instead of removing)
     * - Check if patient has submissions first
     * - Cascade delete related records
     */
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    /**
     * WHAT IS @Transactional?
     *
     * A transaction ensures database operations are atomic:
     * - All operations succeed together, OR
     * - All operations fail together (rollback)
     *
     * Example scenario:
     *   1. Create patient
     *   2. Send welcome email
     *   3. Email fails
     *   → Transaction rolls back, patient is NOT created
     *
     * Without @Transactional:
     *   → Patient would be created but email failed
     *   → Inconsistent state!
     *
     * Spring handles:
     * - Starting transaction before method
     * - Committing if method succeeds
     * - Rolling back if exception thrown
     * - Closing database connection
     */
}
