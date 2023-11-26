package fr.hey.PatataChat.services.security;

import fr.hey.PatataChat.entities.UserEntity;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    UserEntity getUserAuth();
}
