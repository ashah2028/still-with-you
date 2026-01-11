package org.stillwithyou.message;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stillwithyou.patient.Patient;
import org.stillwithyou.patient.PatientService;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final PatientService patientService;

    public MessageService(MessageRepository messageRepository, PatientService patientService) {
        this.messageRepository = messageRepository;
        this.patientService = patientService;
    }

    public Message sendMessage(UUID patientId, String senderName, String content) {
        Patient patient = patientService.getPatient(patientId);

        Message message = new Message();
        message.setPatient(patient);
        message.setSenderName(senderName);
        message.setContent(content);

        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> getPatientMessages(UUID patientId) {
        patientService.getPatient(patientId); // Verify patient exists
        return messageRepository.findByPatientIdOrderBySentAtDesc(patientId);
    }

    @Transactional(readOnly = true)
    public Message getMessage(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    public void deleteMessage(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new IllegalArgumentException("Message not found");
        }
        messageRepository.deleteById(id);
    }
}
