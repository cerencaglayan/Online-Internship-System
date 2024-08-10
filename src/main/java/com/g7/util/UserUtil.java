package com.g7.util;

import com.g7.model.Company;
import com.g7.model.IztechUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserUtil {
    private final UserDetailsService userDetailsService;
    public boolean isCurrentUserIztechStudent() {
        return getCurrentUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_IZTECH_STUDENT"));
    }

    public boolean isCurrentUserCompany() {
        return getCurrentUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"));
    }

    public boolean isCurrentUserInternshipCoordinator() {
        return getCurrentUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INTERNSHIP_COORDINATOR"));
    }

    public UserDetails getCurrentUser() {
        String username = "";

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Giriş Yapan Kullanıcı Bulunamadı: " + "User: " + username);
        }
    }

    public Company getCurrentCompany() {
        return (Company) getCurrentUser();
    }

    public IztechUser getCurrentIztechUser() {
        return (IztechUser) getCurrentUser();
    }

}
