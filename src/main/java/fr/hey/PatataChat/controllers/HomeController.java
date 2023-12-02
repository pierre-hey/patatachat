package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.dto.UserMessageDto;
import fr.hey.PatataChat.entities.Message;
import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.services.MessageService;
import fr.hey.PatataChat.services.UserService;
import fr.hey.PatataChat.services.security.IAuthenticationFacade;
import fr.hey.PatataChat.utils.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping({"index", "/"})
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public HomeController(IAuthenticationFacade authenticationFacade, UserService userService, MessageService messageService) {
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping()
    public ModelAndView home() {

        // Récupération de l'utilisateur connecté
        UserEntity user = this.authenticationFacade.getUserAuth();

        ModelAndView modelAndView = new ModelAndView("index");

        if (!ObjectUtils.isEmpty(user)) {
            List<UserDto> users = userService.findAllUsers();
            modelAndView.addObject("users", users);
            modelAndView.addObject("isAdmin", RoleChecker.isAdmin(user));
        } else {
            // Si login absent/incorrect retourne à la page login
            modelAndView.setViewName("login");
        }

        return modelAndView;

    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public UserMessageDto sendMessage(@Payload UserMessageDto userMessageDTO) {

        UserEntity user = userService.findByLogin(userMessageDTO.getUserName());

        if (!ObjectUtils.isEmpty(user)) {
            Message message = new Message();
            message.setUserEntity(user);
            message.setMessageDateTime(OffsetDateTime.now());
            message.setText(userMessageDTO.getContent());

            messageService.createMessage(message);

            LOGGER.info(MessageFormat.format("L''utilisateur {0} a envoyé un message id: {1}",
                    Optional.of(user).map(UserEntity::getLogin).orElse("ALIEN"), message.getId()));
        }

        return userMessageDTO;
    }

    @PostMapping("/clear")
    public String deleteAllMessage() {

        UserEntity user = authenticationFacade.getUserAuth();

        if (RoleChecker.isAdmin(user)) {
            messageService.deleteAllMessages();
            LOGGER.info(MessageFormat.format("L''utilisateur {0} a supprimé tous les message",
                    Optional.of(user).map(UserEntity::getLogin).orElse("ALIEN")));
        }

        return "redirect:/";
    }


}
