import { useState } from 'react';
import { Message, Patient } from '../types';
import { messageApi } from '../services/api';

interface MessageFormProps {
  patient: Patient;
  onMessageSent: (message: Message) => void;
}

export function MessageForm({ patient, onMessageSent }: MessageFormProps) {
  const [senderName, setSenderName] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!patient.id) return;

    setLoading(true);
    setError('');

    try {
      const message = await messageApi.send({
        patientId: patient.id,
        senderName,
        content,
      });
      onMessageSent(message);
      // Reset form
      setSenderName('');
      setContent('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to send message');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: '2rem' }}>
      <h3>Send Message to {patient.name}</h3>
      {error && <div style={{ color: 'red' }}>{error}</div>}

      <div style={{ marginBottom: '1rem' }}>
        <input
          type="text"
          placeholder="Your Name *"
          value={senderName}
          onChange={(e) => setSenderName(e.target.value)}
          required
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <div style={{ marginBottom: '1rem' }}>
        <textarea
          placeholder="Your message *"
          value={content}
          onChange={(e) => setContent(e.target.value)}
          required
          rows={4}
          style={{ width: '100%', padding: '0.5rem' }}
        />
      </div>

      <button type="submit" disabled={loading} style={{ padding: '0.5rem 1rem' }}>
        {loading ? 'Sending...' : 'Send Message'}
      </button>
    </form>
  );
}
