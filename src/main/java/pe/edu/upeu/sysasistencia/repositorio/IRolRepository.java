package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Rol;
import java.util.Optional;

public interface IRolRepository extends ICrudGenericoRepository<Rol, Long>{
    Optional<Rol> findByNombre(Rol.RolNombre rolNombre);
}
