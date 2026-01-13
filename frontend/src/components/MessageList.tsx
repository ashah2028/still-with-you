import { Message } from '../types';

interface MessageListProps {
  messages: Message[];
}

export function MessageList({ messages }: MessageListProps) {
  return (
    <div>
      <h3>Messages</h3>
      {messages.length === 0 ? (
        <p>No messages yet. Send the first one!</p>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {messages.map((message) => (
            <div
              key={message.id}
              style={{
                padding: '1rem',
                border: '1px solid #ddd',
                borderRadius: '8px',
                backgroundColor: '#f9f9f9',
              }}
            >
              <div style={{ fontWeight: 'bold', marginBottom: '0.5rem' }}>
                {message.senderName}
              </div>
              <div style={{ marginBottom: '0.5rem' }}>{message.content}</div>
              {message.sentAt && (
                <div style={{ fontSize: '0.75rem', color: '#888' }}>
                  {new Date(message.sentAt).toLocaleString()}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
