import { useEffect, useState } from 'react';
import { Patient, Message } from './types';
import { patientApi, messageApi } from './services/api';
import { PatientForm } from './components/PatientForm';
import { PatientList } from './components/PatientList';
import { MessageForm } from './components/MessageForm';
import { MessageList } from './components/MessageList';
import './App.css';

function App() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [selectedPatient, setSelectedPatient] = useState<Patient | null>(null);
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Load patients on mount
  useEffect(() => {
    loadPatients();
  }, []);

  // Load messages when a patient is selected
  useEffect(() => {
    if (selectedPatient?.id) {
      loadMessages(selectedPatient.id);
    } else {
      setMessages([]);
    }
  }, [selectedPatient]);

  const loadPatients = async () => {
    try {
      setLoading(true);
      console.log('Loading patients...');
      const data = await patientApi.getAll();
      console.log('Patients loaded:', data);
      setPatients(data);
    } catch (err) {
      console.error('Error loading patients:', err);
      setError('Failed to load patients. Make sure the backend is running!');
    } finally {
      setLoading(false);
    }
  };

  const loadMessages = async (patientId: string) => {
    try {
      const data = await messageApi.getByPatientId(patientId);
      setMessages(data);
    } catch (err) {
      setError('Failed to load messages');
    }
  };

  const handlePatientCreated = (patient: Patient) => {
    setPatients([...patients, patient]);
    setSelectedPatient(patient);
  };

  const handleMessageSent = (message: Message) => {
    setMessages([message, ...messages]);
  };

  return (
    <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
      <h1>Still With You - Patient Messaging</h1>

      {loading && <div>Loading patients...</div>}

      {error && (
        <div style={{ padding: '1rem', backgroundColor: '#fee', color: 'red', marginBottom: '1rem', borderRadius: '8px' }}>
          {error}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
        {/* Left Column: Patients */}
        <div>
          <PatientForm onPatientCreated={handlePatientCreated} />
          <PatientList
            patients={patients}
            selectedPatient={selectedPatient}
            onSelectPatient={setSelectedPatient}
          />
        </div>

        {/* Right Column: Messages */}
        <div>
          {selectedPatient ? (
            <>
              <MessageForm patient={selectedPatient} onMessageSent={handleMessageSent} />
              <MessageList messages={messages} />
            </>
          ) : (
            <div style={{ padding: '2rem', textAlign: 'center', color: '#888' }}>
              <p>← Select a patient to send messages</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
