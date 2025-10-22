package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.modelo.Rol;
import java.util.Optional;

public interface IRolService extends ICrudGenericoService<Rol, Long>{
    Optional<Rol> getByNombre(Rol.RolNombre rolNombre);
}