package transport.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transport.model.User;
import transport.persistence.repository.UserRepository;
import transport.persistence.repository.utils.PasswordUtils;
import transport.rest.dto.AuthRequest;
import transport.rest.security.JwtUtils;

import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:5174"})
@RequestMapping("/transport/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Utilizator inexistent!"));
        }

        if (!PasswordUtils.verify(authRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Parola incorecta!"));
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
