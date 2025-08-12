package com.best.travel.best_travel.api.models.api.controllers;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.best.travel.best_travel.infraestructure.asbtract_services.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
@Tag(name = "User", description = "User API")
public class AppUserController {
    
    private final IUserService userService;

    @PatchMapping(path = "enabled-or-disabled")
    @Tag(name = "User", description = "Enable or disable a user")
    @Operation(summary = "Enable or disable a user by username")
    public ResponseEntity<Map<String, Boolean>> enabledOrDisabled(@RequestParam String username) {
        var response = this.userService.enabled(username);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "add-role")
    @Tag(name = "User", description = "Add role to a user")
    @Operation(summary = "Add role to a user by username")
    public ResponseEntity<Map<String, Set<String>>> addRole(@RequestParam String username, @RequestParam String role) {
        var response = this.userService.addRole(username, role);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "remove-role")
    @Tag(name = "User", description = "Remove role to a user")
    @Operation(summary = "Remove role to a user by username")
    public ResponseEntity<Map<String, Set<String>>> removeRole(@RequestParam String username, @RequestParam String role) {
        var response = this.userService.removeRole(username, role);
        return ResponseEntity.ok(response);
    }
}
