package pe.edu.upeu.sysasistencia.control;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.sysasistencia.dtos.AccesoDTO;
import pe.edu.upeu.sysasistencia.mappers.AccesoMapper;
import pe.edu.upeu.sysasistencia.servicio.IAccesoService;

import java.util.List;

@RestController
@RequestMapping("/accesos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AccesoController {
    private final IAccesoService accesoService;
    private final AccesoMapper accesoMapper;

    @PostMapping("/user")
    public ResponseEntity<List<AccesoDTO>> getMenusByUser(@RequestBody String username){
        List<AccesoDTO> accesosDTO = accesoMapper.toDTOs(accesoService.getAccesoByUser(username));
        return ResponseEntity.ok(accesosDTO);
    }
}