package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.Sede;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.ISedeRepository;
import pe.edu.upeu.sysasistencia.servicio.ISedeService;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SedeServiceImp extends CrudGenericoServiceImp<Sede, Long> implements ISedeService {
    private final ISedeRepository repo;

    @Override
    protected ICrudGenericoRepository<Sede, Long> getRepo() {
        return repo;
    }

    @Override
    public Optional<Sede> findByNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}