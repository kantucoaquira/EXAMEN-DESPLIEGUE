package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.modelo.Sede;
import java.util.Optional;

public interface ISedeService extends ICrudGenericoService<Sede, Long>{
    Optional<Sede> findByNombre(String nombre);
}