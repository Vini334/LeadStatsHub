package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.UserRequest;
import com.vini334.dashboard.dto.UserResponse;
import com.vini334.dashboard.model.User;
import com.vini334.dashboard.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    private static final String DEFAULT_PASSWORD_HASH = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserResponse> listAll() {
        return userRepository.findAll().stream()
            .map(UserController::toResponse)
            .toList();
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User user = new User();
        user.setUserName(composeFullName(request));
        user.setEmail(normalize(request.email()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUserPassword(DEFAULT_PASSWORD_HASH);
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserRequest request) {
        Optional<User> found = userRepository.findById(id);
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = found.get();
        user.setUserName(composeFullName(request));
        user.setEmail(normalize(request.email()));
        User saved = userRepository.save(user);
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private static UserResponse toResponse(User user) {
        String fullName = Optional.ofNullable(user.getUserName()).orElse("").trim();
        String nome = fullName;
        String sobrenome = "";
        if (!fullName.isEmpty()) {
            int spaceIdx = fullName.indexOf(' ');
            if (spaceIdx > 0) {
                nome = fullName.substring(0, spaceIdx).trim();
                sobrenome = fullName.substring(spaceIdx + 1).trim();
            }
        }
        return new UserResponse(user.getUserId(), nome, sobrenome, user.getEmail());
    }

    private static String composeFullName(UserRequest request) {
        String nome = Optional.ofNullable(request.nome()).orElse("").trim();
        String sobrenome = Optional.ofNullable(request.sobrenome()).orElse("").trim();
        if (nome.isEmpty() && sobrenome.isEmpty()) {
            return "";
        }
        return (nome + " " + sobrenome).trim();
    }

    private static String normalize(String value) {
        return Optional.ofNullable(value).map(String::trim).orElse(null);
    }
}