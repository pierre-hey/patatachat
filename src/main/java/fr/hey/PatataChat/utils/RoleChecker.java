package fr.hey.PatataChat.utils;

import fr.hey.PatataChat.entities.UserEntity;

public class RoleChecker {

    public static boolean isAdmin(UserEntity user) {
        return user.getRoles().stream()
                .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }

}
