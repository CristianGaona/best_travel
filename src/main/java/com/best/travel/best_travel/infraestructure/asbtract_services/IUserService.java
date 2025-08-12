package com.best.travel.best_travel.infraestructure.asbtract_services;

import java.util.Map;
import java.util.Set;

public interface IUserService {
    Map<String, Boolean> enabled(String username);
    Map<String, Set<String>> addRole(String username, String role);
    Map<String, Set<String>> removeRole(String username, String role);
}
