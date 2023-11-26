package fr.hey.PatataChat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMessageDto {

    private String id;

    private String userName;

    private String content;

    private String messageDateTime;

}
