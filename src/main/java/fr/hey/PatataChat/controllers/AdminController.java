package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.services.UserService;
import fr.hey.PatataChat.services.security.IAuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("administration")
public class AdminController {

    private final UserService userService;
    private final IAuthenticationFacade authenticationFacade;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public AdminController(UserService userService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping()
    public ModelAndView adminView() {
        return new ModelAndView("administration/administration");
    }

    @PostMapping("/magicButton")
    public String magicButton() {
        String login = authenticationFacade.getUserAuth().getLogin();
        LOGGER.info("Bouton magique pressé pa l'utilisateur id : {}", login);
        createDevUser();
        return "redirect:/administration";
    }

    private void createDevUser() {
        userService.mockCreateUserIfNotExists("user", List.of("ROLE_USER"));
        userService.mockCreateUserIfNotExists("admin", List.of("ROLE_ADMIN"));
        userService.mockCreateUserIfNotExists("pierre", List.of("ROLE_ADMIN", "ROLE_USER"));

    }

}
