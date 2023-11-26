package fr.hey.PatataChat.repositories;

import fr.hey.PatataChat.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findAllByOrderByMessageDateTimeDesc();

}
