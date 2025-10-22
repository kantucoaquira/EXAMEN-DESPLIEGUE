package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysasistencia.modelo.UsuarioRol;
import pe.edu.upeu.sysasistencia.repositorio.IUsuarioRolRepository;
import pe.edu.upeu.sysasistencia.servicio.IUsuarioRolService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioRolServiceImp implements IUsuarioRolService {
    private final IUsuarioRolRepository repo;

    @Override
    public List<UsuarioRol> findOneByUsuarioUser(String user) {
        return repo.findOneByUsuarioUser(user);
    }

    @Override
    public UsuarioRol save(UsuarioRol ur) {
        return repo.save(ur);
    }
}