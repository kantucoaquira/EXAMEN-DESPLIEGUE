package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.SedeDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.SedeMapper;
import pe.edu.upeu.sysasistencia.modelo.Sede;
import pe.edu.upeu.sysasistencia.servicio.ISedeService;

import java.util.List;

@RestController
@RequestMapping("/sedes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SedeController {
    private final ISedeService sedeService;
    private final SedeMapper sedeMapper;

    @GetMapping
    public ResponseEntity<List<SedeDTO>> findAll() {
        List<SedeDTO> list = sedeMapper.toDTOs(sedeService.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SedeDTO> findById(@PathVariable Long id) {
        Sede obj = sedeService.findById(id);
        return ResponseEntity.ok(sedeMapper.toDTO(obj));
    }

    @PostMapping
    public ResponseEntity<SedeDTO> save(@RequestBody SedeDTO dto) {
        Sede obj = sedeService.save(sedeMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(sedeMapper.toDTO(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeDTO> update(@PathVariable Long id, @RequestBody SedeDTO dto) {
        dto.setIdSede(id);
        Sede obj = sedeService.update(id, sedeMapper.toEntity(dto));
        return ResponseEntity.ok(sedeMapper.toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(sedeService.delete(id));
    }
}