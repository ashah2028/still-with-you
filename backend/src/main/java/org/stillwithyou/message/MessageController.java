package org.stillwithyou.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@Valid @RequestBody MessageRequest request) {
        Message message = messageService.sendMessage(
                request.getPatientId(),
                request.getSenderName(),
                request.getContent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Message>> getPatientMessages(@PathVariable UUID patientId) {
        List<Message> messages = messageService.getPatientMessages(patientId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable UUID id) {
        Message message = messageService.getMessage(id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
