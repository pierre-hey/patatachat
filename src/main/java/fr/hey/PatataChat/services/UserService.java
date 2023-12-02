package fr.hey.PatataChat.services;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.entities.UserEntity;

import java.util.List;

public interface UserService {

    /**
     * Méthode permettant de créer un utilisateur
     *
     * @param userDto Utilisateur à créer
     */
    void saveUser(UserDto userDto);

    /**
     * Méthode permettant de rechercher un utilisateur à partir de son login
     * @param login Login de l'utilisateur
     * @return {@link UserEntity} Utilisateur
     */
    UserEntity findByLogin(String login);

    /**
     * Méthode permettant de recherche tous les utilisateurs
     * @return {@link List} Liste des utilisateurs
     */
    List<UserDto> findAllUsers();

    /**
     * Méthode permettant de créer des utilisateurs à la volée, accessible uniquement avec le profile "dev"
     *
     * @param userInfo information utilisateur
     * @param roles    roles de l'utilisateur
     */
    void mockCreateUserIfNotExists(String userInfo, List<String> roles);

    /**
     * Méthode permettant de mettre à jour les rôles d'un utilisateur (admin/user)
     * @param id Identifiant de l'utilisateur
     */
    UserDto updateUserRole(Integer id);

}
