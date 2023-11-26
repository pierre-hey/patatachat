package fr.hey.PatataChat.services;

import fr.hey.PatataChat.entities.Message;

import java.util.List;

public interface MessageService {

    void createMessage(Message message);

    List<Message> findAllMessages();

    List<Message> findAllMessagesByDateDesc();

    void deleteMessage(Message message);

    void deleteAllMessages();

}
