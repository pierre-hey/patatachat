package fr.hey.PatataChat.services.impl;

import fr.hey.PatataChat.entities.Role;
import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Class implémentant la class UserDetailsService de Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Vérifie que l'utilisateur existe via son identifiant
        UserEntity user = userRepository.findUserByLogin(login);

        // Si l'utilisateur est différent de null, c'est qu'il existe et on peut donc procéder à son identification
        if (!ObjectUtils.isEmpty(user)) {
            return new org.springframework.security.core.userdetails.User(
                    user.getLogin(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));

        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return mapRoles;
    }
}