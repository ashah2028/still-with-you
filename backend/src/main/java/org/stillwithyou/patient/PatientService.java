package org.stillwithyou.patient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByPhoneNumber(patient.getPhoneNumber())) {
            throw new IllegalArgumentException("Patient with phone number already exists");
        }
        return patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Patient getPatient(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    @Transactional(readOnly = true)
    public Patient getPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    @Transactional(readOnly = true)
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(UUID id, Patient updates) {
        Patient patient = getPatient(id);

        if (updates.getName() != null) {
            patient.setName(updates.getName());
        }
        if (updates.getHospitalName() != null) {
            patient.setHospitalName(updates.getHospitalName());
        }
        if (updates.getRoomNumber() != null) {
            patient.setRoomNumber(updates.getRoomNumber());
        }

        return patientRepository.save(patient);
    }

    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new IllegalArgumentException("Patient not found");
        }
        patientRepository.deleteById(id);
    }
}
