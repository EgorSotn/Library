package org.example.home.service;

import lombok.AllArgsConstructor;
import org.example.home.domain.AppUser;
import org.example.home.domain.AppUserRole;
import org.example.home.exception.NotFoundException;

import org.example.home.repository.appuser.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;



//    private final AuthorityRepository authorityRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser =  appUserRepository.findByLogin(username).orElseThrow(()-> new NotFoundException(username));
//        Authority authority = authorityRepository.findByAuthUser(authUser).get();
        return User.builder()
                .username(appUser.getLogin())
                .password(appUser.getPassword())
                .roles(appUser.getAppUserRole().name())
                .build();
    }


}
