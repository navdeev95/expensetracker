package io.github.nikoir.expensetracker.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        // 1. Извлекаем realm roles
        Collection<GrantedAuthority> realmRoles = extractRoles(source);

        // 2. Извлекаем resource roles (из account)
        Collection<GrantedAuthority> resourceRoles = extractResourceRoles(source);

        // 3. Извлекаем scopes
        Collection<GrantedAuthority> scopes = extractScopes(source);

        // 4. Объединяем все authorities
        return Stream.of(realmRoles, resourceRoles, scopes)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }

        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get("account");
        if (resource == null) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) resource.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    private Collection<GrantedAuthority> extractScopes(Jwt jwt) {
        String scope = jwt.getClaim("scope");
        if (scope == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(scope.split(" "))
                .map(scopeName -> new SimpleGrantedAuthority("SCOPE_" + scopeName))
                .collect(Collectors.toList());
    }
}
