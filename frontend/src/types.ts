export interface Patient {
  id?: string;
  name: string;
  phoneNumber: string;
  hospitalName?: string;
  roomNumber?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Message {
  id?: string;
  patient?: Patient;
  senderName: string;
  content: string;
  sentAt?: string;
}

export interface MessageRequest {
  patientId: string;
  senderName: string;
  content: string;
}
