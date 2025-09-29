package com.vini334.dashboard.model.auth;

import jakarta.persistence.*;
import java.util.Set;

// Classe Role removida do JPA, não utilizada pois não há tabela ROLES no banco
public class Role {
    // Se precisar usar para lógica, mantenha simples
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
