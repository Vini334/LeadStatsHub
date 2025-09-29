package com.vini334.dashboard.controller;

import com.vini334.dashboard.dto.InteractionDTO;
import com.vini334.dashboard.model.Interaction;
import com.vini334.dashboard.service.InteractionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;

    @Operation(summary = "Registra uma nova interação")
    @PostMapping
    public ResponseEntity<InteractionDTO> registerInteraction(@RequestBody InteractionDTO dto) {
        Interaction interaction = interactionService.registerInteraction(dto);
        return ResponseEntity.ok(new InteractionDTO(interaction));
    }
}
