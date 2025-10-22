package pe.edu.upeu.sysasistencia.servicio.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.modelo.Persona;
import pe.edu.upeu.sysasistencia.repositorio.ICrudGenericoRepository;
import pe.edu.upeu.sysasistencia.repositorio.IPersonaRepository;
import pe.edu.upeu.sysasistencia.servicio.IPersonaService;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonaServiceImp extends CrudGenericoServiceImp<Persona, Long> implements IPersonaService {
    private final IPersonaRepository repo;

    @Override
    protected ICrudGenericoRepository<Persona, Long> getRepo() {
        return repo;
    }

    @Override
    public Optional<Persona> findByCodigoEstudiante(String codigo) {
        return repo.findByCodigoEstudiante(codigo);
    }

    @Override
    public Optional<Persona> findByDocumento(String documento) {
        return repo.findByDocumento(documento);
    }
}