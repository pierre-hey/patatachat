package fr.hey.PatataChat.services.impl;

import fr.hey.PatataChat.dto.UserDto;
import fr.hey.PatataChat.entities.Role;
import fr.hey.PatataChat.entities.UserEntity;
import fr.hey.PatataChat.exceptions.UserAlreadyExistException;
import fr.hey.PatataChat.repositories.RoleRepository;
import fr.hey.PatataChat.repositories.UserRepository;
import fr.hey.PatataChat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    final private String DEFAULT_USER_ROLE = "ROLE_USER";

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setLogin(userDto.getLogin());

        if (loginExists(userDto.getLogin())) {
            throw new UserAlreadyExistException("Vous ne pouvez pas utiliser ce login !");
        }

        // Crypter le mot de passe
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // Définir le role par défaut
        Role role = roleRepository.findByName(DEFAULT_USER_ROLE);
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        // Enregistrer l'utilisateur
        userRepository.save(user);
    }

    @Override
    public UserEntity findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    @Profile(value = "dev")
    public void mockCreateUserIfNotExists(String userInfo, List<String> roles) {

        if (!loginExists(userInfo)) {
            UserEntity user = new UserEntity();
            user.setLogin(userInfo);
            user.setPassword(passwordEncoder.encode(userInfo));
            user.setRoles(mockCreateRoleIfNotExists(roles));
            mockCreateUser(user);
            System.out.println("####################");
            System.out.printf("Création de l'utilisateur : %s%n", user);
            System.out.println("####################");
        } else {
            System.out.println("####################");
            System.out.println("L'utilisateur existe déjà");
            System.out.println("####################");
        }
    }

    private void mockCreateUser(UserEntity user) {
        userRepository.save(user);
    }

    private List<Role> mockCreateRoleIfNotExists(List<String> rolesName) {

        List<Role> rolesList = new ArrayList<>();
        rolesName.forEach(roleName -> {
            Role role = roleRepository.findByName(roleName);

            if (ObjectUtils.isEmpty(role)) {
                Role roleAdded = new Role();
                roleAdded.setName(roleName);

                roleRepository.save(roleAdded);
                rolesList.add(roleAdded);

                System.out.println("####################");
                System.out.printf("Création du rôle : %s%n", roleAdded);
                System.out.println("####################");


            } else {
                System.out.println("####################");
                System.out.printf("Le role existe déjà : %s%n", role);
                System.out.println("####################");
                rolesList.add(role);
            }

        });
        return rolesList;

    }

    private boolean loginExists(String login) {
        return findByLogin(login) != null;
    }

    /**
     * Convertit un User en UserDto, le mot de passe n'est pas retourné
     *
     * @param user utilisateur
     * @return userDto
     */
    private UserDto convertEntityToDto(UserEntity user) {

        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setId(user.getId());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName(DEFAULT_USER_ROLE);
        return roleRepository.save(role);
    }
}
