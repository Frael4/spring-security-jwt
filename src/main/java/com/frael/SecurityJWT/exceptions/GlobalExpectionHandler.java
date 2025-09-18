package com.frael.SecurityJWT.exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @apiNote Esta clase NO MANEJA las excepciones debido a que esto es un API rest, diferente a como se trabaja en MVC
 * 
 * @author Frael
 */
@RestControllerAdvice
public class GlobalExpectionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handlerNoAuthorized(AccessDeniedException exception) {

        return new ResponseEntity<>("Acceso denegado. No tienes los permisos necesarios.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>("Credenciales de autenticación inválidas.", HttpStatus.UNAUTHORIZED);
    }
}
