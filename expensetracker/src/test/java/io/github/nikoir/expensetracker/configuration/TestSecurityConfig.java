package io.github.nikoir.expensetracker.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public Converter<Jwt, AbstractAuthenticationToken> jwtConverter() {
        return new TestJwtConverter();
    }

    @Bean
    @Primary
    public JwtDecoder mockJwtDecoder() { //заглушка для JwtDecoder, так как он активируется с помощью конфигурации OAuth2 Resource Server
        return _ -> Jwt.withTokenValue("mock-token").build();
    }

}
