package pe.edu.upeu.sysasistencia.servicio;

import pe.edu.upeu.sysasistencia.modelo.Persona;
import java.util.Optional;

public interface IPersonaService extends ICrudGenericoService<Persona, Long>{
    Optional<Persona> findByCodigoEstudiante(String codigo);
    Optional<Persona> findByDocumento(String documento);
}