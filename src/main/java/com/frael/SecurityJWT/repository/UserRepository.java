package com.frael.SecurityJWT.repository;

import java.util.Optional;

//import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import com.frael.SecurityJWT.models.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
 
    // @Procedure -> Esta anotacion solo se usa para con procedimientos
    public Optional<UserEntity> findByUsername(String username);
    
}
