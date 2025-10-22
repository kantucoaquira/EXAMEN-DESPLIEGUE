package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.ProgramaEstudio;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IProgramaEstudioRepository;
import pe.edu.upeu.sysasistencia.servicio.IProgramaEstudioService;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgramaEstudioServiceImp extends CrudGenericoServiceImp<ProgramaEstudio, Long> implements IProgramaEstudioService {
    private final IProgramaEstudioRepository repo;

    @Override
    protected ICrudGenericoRepository<ProgramaEstudio, Long> getRepo() {
        return repo;
    }

    @Override
    public Optional<ProgramaEstudio> findByNombre(String nombre) {
        return repo.findByNombre(nombre);
    }
}