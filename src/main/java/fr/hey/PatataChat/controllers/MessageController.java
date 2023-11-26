package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.dto.UserMessageDto;
import fr.hey.PatataChat.entities.Message;
import fr.hey.PatataChat.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public List<UserMessageDto> getAllMessages() {

        List<Message> messageList = messageService.findAllMessages();
        List<UserMessageDto> userMessageDtoList = new ArrayList<>();
        messageList.forEach(message -> {
            UserMessageDto messageDto = UserMessageDto.builder()
                    .id(String.valueOf(message.getId()))
                    .content(message.getText())
                    .userName(message.getUserEntity().getLogin())
                    .messageDateTime(message.getMessageDateTime().toString())
                    .build();

            userMessageDtoList.add(messageDto);
        });


        return userMessageDtoList;
    }
}