import { useState } from 'react';
import { Patient } from '../types';
import { patientApi } from '../services/api';

interface PatientFormProps {
  onPatientCreated: (patient: Patient) => void;
}

export function PatientForm({ onPatientCreated }: PatientFormProps) {
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [hospitalName, setHospitalName] = useState('');
  const [roomNumber, setRoomNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const patient = await patientApi.create({
        name,
        phoneNumber,
        hospitalName: hospitalName || undefined,
        roomNumber: roomNumber || undefined,
      });
      onPatientCreated(patient);
      // Reset form
      setName('');
      setPhoneNumber('');
      setHospitalName('');
      setRoomNumber('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create patient');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: '2rem' }}>
      <h2>Register Patient</h2>
      {error && <div style={{ color: 'red' }}>{error}</div>}

      <div style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="Name *"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <div style={{ marginBottom: '1rem' }}>
        <input
          type="tel"
          placeholder="Phone Number (e.g., +1234567890) *"
          value={phoneNumber}
          onChange={(e) => setPhoneNumber(e.target.value)}
          required
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <div style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="Hospital Name"
          value={hospitalName}
          onChange={(e) => setHospitalName(e.target.value)}
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <div style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="Room Number"
          value={roomNumber}
          onChange={(e) => setRoomNumber(e.target.value)}
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <button type="submit" disabled={loading} style={{ padding: '0.5rem 1rem' }}>
        {loading ? 'Creating...' : 'Register Patient'}
      </button>
    </form>
  );
}
