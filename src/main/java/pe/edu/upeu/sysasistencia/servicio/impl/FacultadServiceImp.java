package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.Facultad;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IFacultadRepository;
import pe.edu.upeu.sysasistencia.servicio.IFacultadService;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacultadServiceImp extends CrudGenericoServiceImp<Facultad, Long> implements IFacultadService {
    private final IFacultadRepository repo;

    @Override
    protected ICrudGenericoRepository<Facultad, Long> getRepo() {
        return repo;
    }

    @Override
    public Optional<Facultad> findByNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}