package com.vini334.dashboard.service;

import com.vini334.dashboard.repository.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final InteractionRepository interactionRepository;

    public DashboardService(UserRepository userRepository, PostRepository postRepository, InteractionRepository interactionRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.interactionRepository = interactionRepository;
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalPosts() {
        return postRepository.count();
    }

    public long getTotalLikes() {
        return interactionRepository.countByType("LIKE");
    }
}
