package fr.hey.PatataChat.controllers;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.exceptions.UserAlreadyExistException;
import fr.hey.PatataChat.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public ModelAndView showRegistrationForm(){
        return new ModelAndView("register","userDto",new UserDto());
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult result, Model model){

      if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "register";
        }

        try {
            userService.saveUser(userDto);
        }catch (final UserAlreadyExistException existException){

            result.rejectValue("login", null, existException.getMessage());
            return "register";
        }

        return "redirect:/users";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){

        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "users";
    }


    @PostMapping("/magicButton")
    public String magicButton()
    {
        System.out.println("############################");
        System.out.println("BOUTON MAGIQUE PRESSE");
        System.out.println("############################");
        createDevUser();
        return "redirect:/index";
    }
    private void createDevUser() {
        userService.mockCreateUserIfNotExists("user",List.of("ROLE_USER"));
        userService.mockCreateUserIfNotExists("admin",List.of("ROLE_ADMIN"));
        userService.mockCreateUserIfNotExists("pierre", List.of("ROLE_ADMIN", "ROLE_USER"));

    }

}