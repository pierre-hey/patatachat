package fr.hey.PatataChat.services.security;

import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    private final UserService userService;

    @Autowired
    public AuthenticationFacade(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserEntity getUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        if(!ObjectUtils.isEmpty(name)){
            return userService.findByLogin(name);
        }else {
            return null;
        }
    }
}