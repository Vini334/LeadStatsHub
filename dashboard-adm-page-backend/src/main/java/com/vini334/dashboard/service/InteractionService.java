
package com.vini334.dashboard.service;
import com.vini334.dashboard.model.User;
import com.vini334.dashboard.dto.InteractionDTO;
import com.vini334.dashboard.model.Interaction;
import com.vini334.dashboard.model.Post;
import com.vini334.dashboard.repository.InteractionRepository;
import com.vini334.dashboard.repository.PostRepository;
import com.vini334.dashboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionService {
    @Autowired
    private InteractionRepository interactionRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Cria uma interação usando a entidade User já recuperada.
     */
    @Transactional
    public Interaction createInteraction(InteractionDTO dto, User user) {
        Post post = postRepository.findById(dto.getPostId()).orElseThrow();
        Interaction interaction = new Interaction();
        interaction.setPost(post);
        interaction.setUser(user);
        interaction.setType(dto.getType());
        interaction.setCreatedAt(java.time.LocalDateTime.now());
        return interactionRepository.save(interaction);
    }

    /**
     * Método compatível com o controller para registrar uma interação via DTO.
     */
    @Transactional
    public Interaction registerInteraction(InteractionDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        Interaction interaction = new Interaction();
        interaction.setPost(post);
        interaction.setUser(user);
        interaction.setType(dto.getType());
        interaction.setCreatedAt(java.time.LocalDateTime.now());
        return interactionRepository.save(interaction);
    }
}
