package com.frael.SecurityJWT.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.frael.SecurityJWT.security.jwt.JwtUtils;
import com.frael.SecurityJWT.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        // Obtenemos el token del Header
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){
            log.debug("tokenHeader ".concat(tokenHeader));
            String token = tokenHeader.substring(7);

            if(jwtUtils.isTokenValid(token)){// Si el token es valido extraemos el Claim Subject que tiene el username
                String username = jwtUtils.getSubjectFromToken(token);

                // Buscamos el usuario en la base y obtenemos el UserDetails
                UserDetails user = userDetailsServiceImpl.loadUserByUsername(username);

                // TODO: Repasar esta clase
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                log.error("token no valido");
            }

        }else{
            log.debug("No hay token");
        }

        filterChain.doFilter(request, response);
    }
    
}
