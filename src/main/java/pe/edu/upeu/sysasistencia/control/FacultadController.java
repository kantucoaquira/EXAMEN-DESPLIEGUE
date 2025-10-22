package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.FacultadDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.FacultadMapper;
import pe.edu.upeu.sysasistencia.modelo.Facultad;
import pe.edu.upeu.sysasistencia.servicio.IFacultadService;

import java.util.List;

@RestController
@RequestMapping("/facultades")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FacultadController {
    private final IFacultadService facultadService;
    private final FacultadMapper facultadMapper;

    @GetMapping
    public ResponseEntity<List<FacultadDTO>> findAll() {
        List<FacultadDTO> list = facultadMapper.toDTOs(facultadService.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultadDTO> findById(@PathVariable Long id) {
        Facultad obj = facultadService.findById(id);
        return ResponseEntity.ok(facultadMapper.toDTO(obj));
    }

    @PostMapping
    public ResponseEntity<FacultadDTO> save(@RequestBody FacultadDTO dto) {
        Facultad obj = facultadService.save(facultadMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(facultadMapper.toDTO(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultadDTO> update(@PathVariable Long id, @RequestBody FacultadDTO dto) {
        dto.setIdFacultad(id);
        Facultad obj = facultadService.update(id, facultadMapper.toEntity(dto));
        return ResponseEntity.ok(facultadMapper.toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(facultadService.delete(id));
    }
}