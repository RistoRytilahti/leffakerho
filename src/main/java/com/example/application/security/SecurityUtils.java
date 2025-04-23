package com.example.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtils {

    // Tarkistaa, onko käyttäjä kirjautunut sisään ja onko hänellä tietty rooli
    public static boolean isUserInRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // Käyttäjä ei ole kirjautunut sisään
        }

        // Haetaan käyttäjä ja tarkistetaan roolit
        User principal = (User) authentication.getPrincipal();
        return principal.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
    }
}
