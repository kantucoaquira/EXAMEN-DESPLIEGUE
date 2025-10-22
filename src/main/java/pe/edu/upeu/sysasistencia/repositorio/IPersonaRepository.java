package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Persona;
import java.util.Optional;

public interface IPersonaRepository extends ICrudGenericoRepository<Persona, Long>{
    Optional<Persona> findByCodigoEstudiante(String codigoEstudiante);
    Optional<Persona> findByDocumento(String documento);
}