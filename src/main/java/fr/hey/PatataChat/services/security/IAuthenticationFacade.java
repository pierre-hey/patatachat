package fr.hey.PatataChat.services.security;

import fr.hey.PatataChat.entities.UserEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UserEntity getUserAuth();

    List<String> getAllUserAuth();
}
