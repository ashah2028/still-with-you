package org.stillwithyou.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Patient Repository - handles all database operations for Patient entity
 *
 * This is the "Data Access Layer" - it's your connection to the database.
 *
 * THE MAGIC: You don't write any SQL or implementation code!
 * Spring Data JPA automatically generates the implementation at runtime.
 */
@Repository  // Marks this as a Spring bean that handles database access
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    //                                                      ^^^^^^  ^^^^
    //                                                      Entity  ID Type

    /**
     * BUILT-IN METHODS (inherited from JpaRepository):
     * You get these for FREE without writing any code:
     *
     * - save(patient)           → INSERT or UPDATE
     * - findById(id)            → SELECT * FROM patients WHERE id = ?
     * - findAll()               → SELECT * FROM patients
     * - deleteById(id)          → DELETE FROM patients WHERE id = ?
     * - existsById(id)          → SELECT COUNT(*) FROM patients WHERE id = ?
     * - count()                 → SELECT COUNT(*) FROM patients
     *
     * Plus 20+ more methods for pagination, sorting, batching, etc.
     */

    /**
     * CUSTOM QUERY METHODS:
     * Spring Data JPA reads the method name and generates SQL automatically!
     *
     * Method name pattern: findBy + FieldName + Condition
     */

    /**
     * Find patient by email
     * Spring generates: SELECT * FROM patients WHERE email = ?
     * Returns Optional because patient might not exist
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Check if email already exists
     * Spring generates: SELECT EXISTS(SELECT 1 FROM patients WHERE email = ?)
     * Used for validation before creating new patients
     */
    boolean existsByEmail(String email);

    /**
     * MORE EXAMPLES (not implemented, just showing what's possible):
     *
     * List<Patient> findByHospitalName(String hospitalName);
     *   → SELECT * FROM patients WHERE hospital_name = ?
     *
     * List<Patient> findByNameContaining(String keyword);
     *   → SELECT * FROM patients WHERE name LIKE %keyword%
     *
     * List<Patient> findByCreatedAtAfter(LocalDateTime date);
     *   → SELECT * FROM patients WHERE created_at > ?
     *
     * You can also write custom SQL with @Query annotation if needed:
     *
     * @Query("SELECT p FROM Patient p WHERE p.email LIKE %:domain")
     * List<Patient> findByEmailDomain(@Param("domain") String domain);
     */
}
