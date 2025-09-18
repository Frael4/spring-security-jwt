package com.frael.SecurityJWT.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

/**
 * Aqui se define la entidad RoleEntity
 * no es neceserio establecer una bidireccionalidad
 * con la entidad UserEntity
 * 
 * @author Frael
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Importante para que se guanden los String en la base de datos, default ordinal
    //@Column(unique = true)
    private ERole name;
}
