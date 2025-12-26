package org.stillwithyou.patient;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Patient Entity - represents a hospital patient in the database
 *
 * This is the "Model" layer in MVC - it defines:
 * 1. The database table structure (columns, types, constraints)
 * 2. How Java objects map to database rows
 * 3. Validation rules for the data
 */
@Entity  // Tells Spring this is a JPA entity that maps to a database table
@Table(name = "patients")  // Specifies the exact table name in PostgreSQL
public class Patient {

    /**
     * Primary Key - uniquely identifies each patient
     * Using UUID instead of auto-increment numbers because:
     * - UUIDs are globally unique (good for distributed systems)
     * - Can't guess other patient IDs (security)
     * - Can generate IDs before saving to database
     */
    @Id  // Marks this as the primary key
    @GeneratedValue(strategy = GenerationType.UUID)  // Auto-generate UUIDs
    private UUID id;

    /**
     * Patient's full name
     * @NotBlank - ensures it's not null, empty, or just whitespace
     * nullable = false - enforces NOT NULL constraint in database
     */
    @NotBlank(message = "Patient name is required")
    @Column(nullable = false)
    private String name;

    /**
     * Patient's email - used for authentication
     * @Email - validates email format
     * unique = true - ensures no duplicate emails in database
     */
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Optional hospital information
     * No validation constraints = these fields can be null
     * Using snake_case column names to match PostgreSQL conventions
     */
    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "room_number")
    private String roomNumber;

    /**
     * Audit timestamps - automatically track when records are created/updated
     * updatable = false on createdAt means it never changes after creation
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * JPA Lifecycle Hooks - automatically called by Spring
     * @PrePersist runs before the entity is first saved to database
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate runs before the entity is updated in database
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== GETTERS & SETTERS =====
    // Spring JPA requires these to access/modify fields
    // In production, you'd use Lombok @Data to auto-generate these

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
