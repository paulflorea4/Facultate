package org.example.inchirieriauto.service;

import jakarta.transaction.Transactional;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.exception.EmailAlreadyUsedException;
import org.example.inchirieriauto.exception.UserNotFoundException;
import org.example.inchirieriauto.model.Client;
import org.example.inchirieriauto.model.User;
import org.example.inchirieriauto.model.UserType;
import org.example.inchirieriauto.repository.ClientRepository;
import org.example.inchirieriauto.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       ClientRepository clientRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void registerClient(String email, String password, String name){
        if (emailExists(email)) {
            throw new EmailAlreadyUsedException();
        }

        Client client = new Client();
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        client.setName(name);
        client.setType(UserType.CLIENT);
        try {
            clientRepository.save(client);
        } catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyUsedException();
        }
    }

    public Client requireClientByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (user instanceof Client client) {
            return client;
        }

        throw new BusinessRuleException("Doar clienții pot face o rezervare.");
    }
}
