package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.PersonaDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.PersonaMapper;
import pe.edu.upeu.sysasistencia.modelo.Persona;
import pe.edu.upeu.sysasistencia.servicio.IPersonaService;

import java.util.List;

@RestController
@RequestMapping("/personas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PersonaController {
    private final IPersonaService personaService;
    private final PersonaMapper personaMapper;

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> findAll() {
        List<PersonaDTO> list = personaMapper.toDTOs(personaService.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> findById(@PathVariable Long id) {
        Persona obj = personaService.findById(id);
        return ResponseEntity.ok(personaMapper.toDTO(obj));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PersonaDTO> findByCodigo(@PathVariable String codigo) {
        Persona obj = personaService.findByCodigoEstudiante(codigo)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        return ResponseEntity.ok(personaMapper.toDTO(obj));
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> save(@RequestBody PersonaDTO dto) {
        Persona obj = personaService.save(personaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(personaMapper.toDTO(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> update(@PathVariable Long id, @RequestBody PersonaDTO dto) {
        dto.setIdPersona(id);
        Persona obj = personaService.update(id, personaMapper.toEntity(dto));
        return ResponseEntity.ok(personaMapper.toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.delete(id));
    }
}