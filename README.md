# Spring Security JWT
Aprendiendo Spring security con JSON Web Tokens

## Testeo de Enpoind

`POST /api/auth/createUser` - Creación de un usuario, metodo protegido con **Autenticación JWT**

![alt text](images-readme/image.png)

### Manejo de roles

Para restringuir el acceso a los endpoints existen dos formas

Usando anotaciones, habilitando `@EnableGlobalMethodSecurity` en la clase de configuración de SpringSecurity

```JAVA

@RestController
@RequestMapping("/api/role")
public class TestRoleController {
    
    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin() {
        return "Hola, has accedido con el rol de ADMIN";
    }

    @GetMapping("/accessUser")
    @PreAuthorize("hasRole('USER')")
    public String accessUser() {
        return "Hola, has accedido con el rol de USER";
    }

    @GetMapping("/accessAnyRole")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String accessAnyRole(@RequestParam String param) {
        return new String("Hola, has accedido con cualquier ROL");
    }
    
}
```