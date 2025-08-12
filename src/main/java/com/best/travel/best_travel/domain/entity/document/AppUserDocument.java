package com.best.travel.best_travel.domain.entity.document;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "app_users")
public class AppUserDocument implements Serializable {
    
    @Id
    private String id;
    private String dni;
    private String username;
    private boolean enabled;
    private String password;
    private Role role;

    
}
