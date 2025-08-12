package com.best.travel.best_travel.domain.entity.document;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Field(name = "granted_authorities")
    private Set<String> grantedAuthorities;
}
