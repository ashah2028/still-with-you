import { Patient } from '../types';

interface PatientListProps {
  patients: Patient[];
  selectedPatient: Patient | null;
  onSelectPatient: (patient: Patient) => void;
}

export function PatientList({ patients, selectedPatient, onSelectPatient }: PatientListProps) {
  return (
    <div style={{ marginBottom: '2rem' }}>
      <h2>Patients</h2>
      {patients.length === 0 ? (
        <p>No patients registered yet.</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
          {patients.map((patient) => (
            <div
              key={patient.id}
              onClick={() => onSelectPatient(patient)}
              style={{
                padding: '1rem',
                border: selectedPatient?.id === patient.id ? '2px solid #646cff' : '1px solid #ccc',
                borderRadius: '8px',
                cursor: 'pointer',
                backgroundColor: selectedPatient?.id === patient.id ? '#f0f0ff' : 'white',
              }}
            >
              <div style={{ fontWeight: 'bold' }}>{patient.name}</div>
              <div style={{ fontSize: '0.9rem', color: '#666' }}>{patient.phoneNumber}</div>
              {patient.hospitalName && (
                <div style={{ fontSize: '0.85rem', color: '#888' }}>
                  {patient.hospitalName} {patient.roomNumber && `- Room ${patient.roomNumber}`}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
