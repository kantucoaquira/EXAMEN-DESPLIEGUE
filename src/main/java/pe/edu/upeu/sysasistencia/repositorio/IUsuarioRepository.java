package pe.edu.upeu.sysasistencia.repositorio;

import pe.edu.upeu.sysasistencia.modelo.Usuario;
import java.util.Optional;

public interface IUsuarioRepository extends ICrudGenericoRepository<Usuario, Long>{
    Optional<Usuario> findOneByUser(String user);
}