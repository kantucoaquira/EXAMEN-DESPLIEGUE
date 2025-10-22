package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.Rol;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IRolRepository;
import pe.edu.upeu.sysasistencia.servicio.IRolService;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RolServiceImp extends CrudGenericoServiceImp<Rol, Long> implements IRolService {
    private final IRolRepository repo;

    @Override
    protected ICrudGenericoRepository<Rol, Long> getRepo() {
        return repo;
    }

    @Override
    public Optional<Rol> getByNombre(Rol.RolNombre rolNombre) {
        return repo.findByNombre(rolNombre);
    }
}