package fr.hey.PatataChat.repositories;

import fr.hey.PatataChat.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findUserByLogin(String login);
}