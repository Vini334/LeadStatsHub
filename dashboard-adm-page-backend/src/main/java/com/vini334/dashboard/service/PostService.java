package com.vini334.dashboard.service;

import com.vini334.dashboard.dto.PostDTO;
import com.vini334.dashboard.model.Post;
import com.vini334.dashboard.model.User;
import com.vini334.dashboard.repository.PostRepository;
import com.vini334.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

        /**
         * Cria um post usando a entidade User já recuperada.
         */
        @Transactional
        public Post createPost(PostDTO postDto, User user) {
            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setUser(user);
            post.setCreatedAt(java.time.LocalDateTime.now());
            return postRepository.save(post);
        }
}
