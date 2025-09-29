package com.vini334.dashboard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "INTERACTIONS")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interaction_seq")
    @SequenceGenerator(name = "interaction_seq", sequenceName = "INTERACTION_SEQ", allocationSize = 1)
    @Column(name = "INTERACTION_ID")
    private Long interactionId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // Getters e Setters
    public Long getInteractionId() { return interactionId; }
    public void setInteractionId(Long interactionId) { this.interactionId = interactionId; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
