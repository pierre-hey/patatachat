package fr.hey.PatataChat.services;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.entities.UserEntity;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    UserEntity findByLogin(String login);

    List<UserDto> findAllUsers();

    /**
     * Méthode permettant de créer des utilisateurs à la volée, accessible uniquement avec le profile "dev"
     *
     * @param userInfo information utilisateur
     * @param roles    roles de l'utilisateur
     */
    void mockCreateUserIfNotExists(String userInfo, List<String> roles);

}
