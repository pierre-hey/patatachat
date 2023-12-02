package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.services.UserService;
import fr.hey.PatataChat.services.security.IAuthenticationFacade;
import fr.hey.PatataChat.utils.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserController(UserService userService, IAuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
    }


    @GetMapping("/all")
    public ModelAndView viewALlUsers() {

        List<UserDto> allUsers = userService.findAllUsers();
        List<String> loggedUsers = authenticationFacade.getAllUserAuth();

        loggedUsers.forEach(userName -> allUsers.forEach(userDto -> {
            if (userName.equals(userDto.getLogin())) {
                userDto.setIsConnected(true);
            }
        }));

        ModelAndView modelAndView = new ModelAndView("user/users");
        modelAndView.addObject("userList", allUsers);

        return modelAndView;
    }

    @PostMapping("/update/role")
    public String updateUserRole(@RequestParam("id") Integer id, RedirectAttributes redirAttrs) {

        String route = "user/all";
        UserEntity userAuth = authenticationFacade.getUserAuth();
        boolean isAdmin = RoleChecker.isAdmin(userAuth);
        boolean userAuthModifyHimself = Objects.equals(userAuth.getId(), id);

        if (userAuthModifyHimself) {
            redirAttrs.addFlashAttribute("msgFlash", "Vous ne pouvez pas modifier votre propre rôle !");
        }
        if (isAdmin && !userAuthModifyHimself) {
            LOGGER.info("Modification du rôle de l'utilisateur id: {} par l'utilisateur id: {}", id, userAuth.getId());
            UserDto userDto = userService.updateUserRole(id);
            route = "user/all";
            redirAttrs.addFlashAttribute("msgFlash", MessageFormat.format(
                    "Rôle de l''utilisateur {0} modifié avec succès", userDto.getLogin()));
        }

        return "redirect:/" + route;
    }

}
