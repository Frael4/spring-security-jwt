package com.frael.SecurityJWT.services;

import java.util.Collection;
//import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.frael.SecurityJWT.models.UserEntity;
import com.frael.SecurityJWT.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Creamos una coleccion de tipo de datos que extiende de GrantedAuthority
        // llenanos esta collecion con los roles de userEntity, mapeando y casteando cada rol a SimpleGrantedAuthority
        // y al final lo hacemos un set y no a lista porque lista permite duplicados
        Collection<? extends GrantedAuthority> authorities = userEntity.getRoles().stream().map( r -> new SimpleGrantedAuthority("ROLE_".concat(r.getName().name())) ).collect(Collectors.toSet());
            
        return new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, authorities);
    }

}
