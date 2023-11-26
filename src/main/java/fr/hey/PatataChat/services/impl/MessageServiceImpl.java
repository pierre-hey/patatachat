package fr.hey.PatataChat.services.impl;

import fr.hey.PatataChat.entities.Message;
import fr.hey.PatataChat.repositories.MessageRepository;
import fr.hey.PatataChat.services.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllMessagesByDateDesc() {
        return messageRepository.findAllByOrderByMessageDateTimeDesc();
    }

    @Override
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public void deleteAllMessages() {
        messageRepository.deleteAll();
    }
}
