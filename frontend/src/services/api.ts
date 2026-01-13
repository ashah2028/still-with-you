import { Patient, Message, MessageRequest } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

// Patient API
export const patientApi = {
  async create(patient: Omit<Patient, 'id' | 'createdAt' | 'updatedAt'>): Promise<Patient> {
    const response = await fetch(`${API_BASE_URL}/patients`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(patient),
    });
    if (!response.ok) throw new Error('Failed to create patient');
    return response.json();
  },

  async getAll(): Promise<Patient[]> {
    const response = await fetch(`${API_BASE_URL}/patients`);
    if (!response.ok) throw new Error('Failed to fetch patients');
    return response.json();
  },

  async getById(id: string): Promise<Patient> {
    const response = await fetch(`${API_BASE_URL}/patients/${id}`);
    if (!response.ok) throw new Error('Failed to fetch patient');
    return response.json();
  },

  async update(id: string, updates: Partial<Patient>): Promise<Patient> {
    const response = await fetch(`${API_BASE_URL}/patients/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updates),
    });
    if (!response.ok) throw new Error('Failed to update patient');
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/patients/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete patient');
  },
};

// Message API
export const messageApi = {
  async send(message: MessageRequest): Promise<Message> {
    const response = await fetch(`${API_BASE_URL}/messages`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(message),
    });
    if (!response.ok) throw new Error('Failed to send message');
    return response.json();
  },

  async getByPatientId(patientId: string): Promise<Message[]> {
    const response = await fetch(`${API_BASE_URL}/messages/patient/${patientId}`);
    if (!response.ok) throw new Error('Failed to fetch messages');
    return response.json();
  },

  async delete(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/messages/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete message');
  },
};
