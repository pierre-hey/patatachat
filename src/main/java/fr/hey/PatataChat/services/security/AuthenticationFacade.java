package fr.hey.PatataChat.services.security;

import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    private final UserService userService;

    private final SessionRegistry sessionRegistry;

    @Autowired
    public AuthenticationFacade(UserService userService, SessionRegistry sessionRegistry) {
        this.userService = userService;
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserEntity getUserAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        if (!ObjectUtils.isEmpty(name)) {
            return userService.findByLogin(name);
        } else {
            return null;
        }
    }

    @Override
    public List<String> getAllUserAuth() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .toList();

        List<String> allUser = new ArrayList<>();
        for (Object principal : allPrincipals) {
            if (principal instanceof User) {
                allUser.add(((User) principal).getUsername());
            }
        }

        return allUser;
    }
}