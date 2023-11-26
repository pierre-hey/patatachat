package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.entities.Message;
import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.services.MessageService;
import fr.hey.PatataChat.services.UserService;
import fr.hey.PatataChat.services.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.OffsetDateTime;
import java.util.List;


@Controller
@RequestMapping({"index", "/"})
public class HomeController {

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
        if (!ObjectUtils.isEmpty(user)) {
            ModelAndView modelAndView = new ModelAndView("index");

            List<UserDto> users = userService.findAllUsers();
            modelAndView.addObject("users", users);
            modelAndView.addObject("message",new Message());
            modelAndView.addObject("messages",messageService.findAllMessagesByDateDesc());

            return modelAndView;
        }

        // Si login absent/incorrect retourne à la page login
        return new ModelAndView("login");

    }


    @PostMapping("/add")
    public String addMessage(Message message){
        UserEntity user = authenticationFacade.getUserAuth();

        message.setUserEntity(user);
        message.setMessageDateTime(OffsetDateTime.now());
        messageService.createMessage(message);

        return "redirect:/";
    }

    @PostMapping("/clear")
    public String deleteAllMessage(){
        UserEntity user = authenticationFacade.getUserAuth();

       messageService.deleteAllMessages();

        return "redirect:/";
    }


}
