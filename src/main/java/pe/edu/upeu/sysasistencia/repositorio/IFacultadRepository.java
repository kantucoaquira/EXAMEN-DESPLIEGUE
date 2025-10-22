package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Facultad;
import java.util.Optional;

public interface IFacultadRepository extends ICrudGenericoRepository<Facultad, Long>{
    Optional<Facultad> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}