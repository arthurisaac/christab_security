package com.bafagroupe.christab_security.security;

import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Service
public class UserDetailsServiceI implements UserDetailsService {

    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = accountService.loadUserByEmail(email);
        if(user == null) throw new UsernameNotFoundException("Email does not exist");
        if(!user.getActivated()) throw new UsernameNotFoundException("Username not found"); //pour interdire l'accès de l'application aux comptes désactivés
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(r ->{
			authorities.add(new SimpleGrantedAuthority(r.getRolename()));
		});
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
