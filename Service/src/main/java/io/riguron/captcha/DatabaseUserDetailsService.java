package io.riguron.captcha;

import io.riguron.captcha.repository.UserProfileRepository;
import io.riguron.captcha.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Primary
public class DatabaseUserDetailsService implements UserDetailsService {

    private UserProfileRepository repository;

    @Autowired
    public DatabaseUserDetailsService(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.getCredentials(username)
                .map(u -> new UserData(u.getLogin(), u.getPassword(), u.getId()))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("The username %s doesn't exist", username)));
    }
}
