package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.PostDTO;
import com.vini334.dashboard.model.Post;
import com.vini334.dashboard.model.User;
import com.vini334.dashboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private com.vini334.dashboard.repository.UserRepository userRepository;

    @Operation(summary = "Cria um novo post")
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDto, @RequestParam Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Post post = postService.createPost(postDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostDTO(post));
    }
}
