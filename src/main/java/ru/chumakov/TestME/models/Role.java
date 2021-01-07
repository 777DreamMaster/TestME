package ru.chumakov.TestME.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    CURATOR,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
