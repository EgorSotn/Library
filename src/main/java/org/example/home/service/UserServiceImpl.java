package org.example.home.service;

import lombok.AllArgsConstructor;
import org.example.home.domain.AppUser;
import org.example.home.domain.AppUserRole;
import org.example.home.exception.NotFoundException;
import org.example.home.repository.appuser.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional
    public AppUser saveUser(AppUser user){
        Optional<AppUser> appUser = appUserRepository.findByLogin(user.getLogin());
        if(!appUser.isEmpty()){
            return null;
        }
        user.setAppUserRole(AppUserRole.valueOf("USER"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }
}
