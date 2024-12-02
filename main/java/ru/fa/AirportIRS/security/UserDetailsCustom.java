package ru.fa.AirportIRS.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.fa.AirportIRS.models.Person;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsCustom  implements UserDetails {
    @Getter
    private String username;
    private String password;
    @Getter
    private Set<String> roles;

    public static UserDetailsCustom build(Person person) {
        return new UserDetailsCustom(
                person.getUsername(),
                person.getPassword(),
                person.getRoles()
        );
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
