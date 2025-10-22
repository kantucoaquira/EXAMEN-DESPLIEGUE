package pe.edu.upeu.sysasistencia.repositorio;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.sysasistencia.modelo.UsuarioRol;
import pe.edu.upeu.sysasistencia.modelo.UsuarioRolPK;
import java.util.List;

public interface IUsuarioRolRepository extends ICrudGenericoRepository<UsuarioRol, UsuarioRolPK>{
    @Query("SELECT ur FROM UsuarioRol ur WHERE ur.usuario.user = :user")
    List<UsuarioRol> findOneByUsuarioUser(@Param("user") String user);
}