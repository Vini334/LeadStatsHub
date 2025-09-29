package com.vini334.dashboard.dto;

import com.vini334.dashboard.model.Interaction;

public class InteractionDTO {
    private Long interactionId;
    private Long postId;
    private Long userId;
    private String type;

    public InteractionDTO() {}
    public InteractionDTO(Interaction interaction) {
        this.interactionId = interaction.getInteractionId();
        this.postId = interaction.getPost().getPostId();
        this.userId = interaction.getUser().getUserId();
        this.type = interaction.getType();
    }
    // Getters e setters
    public Long getInteractionId() { return interactionId; }
    public void setInteractionId(Long interactionId) { this.interactionId = interactionId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
