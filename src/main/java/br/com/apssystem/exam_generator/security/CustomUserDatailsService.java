package br.com.apssystem.exam_generator.security;

import br.com.apssystem.exam_generator.persistence.model.ApplicationUser;
import br.com.apssystem.exam_generator.persistence.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDatailsService implements UserDetailsService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ApplicationUser> user = applicationUserRepository.findByUsername(username);
        return user.map(CustomUserDatails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
