package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.ProgramaEstudioDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.ProgramaEstudioMapper;
import pe.edu.upeu.sysasistencia.modelo.ProgramaEstudio;
import pe.edu.upeu.sysasistencia.servicio.IProgramaEstudioService;

import java.util.List;

@RestController
@RequestMapping("/programas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProgramaEstudioController {
    private final IProgramaEstudioService programaService;
    private final ProgramaEstudioMapper programaMapper;

    @GetMapping
    public ResponseEntity<List<ProgramaEstudioDTO>> findAll() {
        List<ProgramaEstudioDTO> list = programaMapper.toDTOs(programaService.findAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramaEstudioDTO> findById(@PathVariable Long id) {
        ProgramaEstudio obj = programaService.findById(id);
        return ResponseEntity.ok(programaMapper.toDTO(obj));
    }

    @PostMapping
    public ResponseEntity<ProgramaEstudioDTO> save(@RequestBody ProgramaEstudioDTO dto) {
        ProgramaEstudio obj = programaService.save(programaMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(programaMapper.toDTO(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgramaEstudioDTO> update(@PathVariable Long id, @RequestBody ProgramaEstudioDTO dto) {
        dto.setIdPrograma(id);
        ProgramaEstudio obj = programaService.update(id, programaMapper.toEntity(dto));
        return ResponseEntity.ok(programaMapper.toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(programaService.delete(id));
    }

    @GetMapping("/buscarmaxid")
    public ResponseEntity<Long> buscarMaxId() {
        List<ProgramaEstudio> programas = programaService.findAll();
        if (programas.isEmpty()) {
            return ResponseEntity.ok(0L);
        }
        Long maxId = programas.stream()
                .map(ProgramaEstudio::getIdPrograma)
                .max(Long::compareTo)
                .orElse(0L);
        return ResponseEntity.ok(maxId);
    }
}