package pe.edu.upeu.sysasistencia.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.upeu.sysasistencia.dtos.SedeDTO;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.mappers.SedeMapper;
import pe.edu.upeu.sysasistencia.modelo.Sede;
import pe.edu.upeu.sysasistencia.servicio.ISedeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class SedeControllerTest {

    @Mock
    private ISedeService sedeService;

    @Mock
    private SedeMapper sedeMapper;

    @InjectMocks
    private SedeController sedeController;

    private Sede sede;
    private SedeDTO sedeDTO;
    private List<Sede> sedes;

    private static final Logger logger = Logger.getLogger(SedeControllerTest.class.getName());

    @BeforeEach
    void setUp() {
        // Preparar Sede
        sede = Sede.builder()
                .idSede(1L)
                .nombre("Sede Principal")
                .descripcion("Sede principal de la universidad")
                .build();

        // Preparar DTO
        sedeDTO = new SedeDTO();
        sedeDTO.setIdSede(1L);
        sedeDTO.setNombre("Sede Principal");
        sedeDTO.setDescripcion("Sede principal de la universidad");

        sedes = List.of(sede);
    }

    @Test
    public void testFindAll_ReturnsListOfSedeDTO_WithHttpStatusOK() {
        // Given
        BDDMockito.given(sedeService.findAll()).willReturn(sedes);
        BDDMockito.given(sedeMapper.toDTOs(sedes)).willReturn(List.of(sedeDTO));

        // When
        ResponseEntity<List<SedeDTO>> response = sedeController.findAll();

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().size());
        Assertions.assertEquals(sedeDTO.getNombre(), response.getBody().get(0).getNombre());

        // Log
        for (SedeDTO s : response.getBody()) {
            logger.info(String.format("SedeDTO{id=%d, nombre='%s', descripcion='%s'}",
                    s.getIdSede(), s.getNombre(), s.getDescripcion()));
        }

        BDDMockito.then(sedeService).should().findAll();
        BDDMockito.then(sedeMapper).should().toDTOs(sedes);
    }

    @Test
    void testFindById_ReturnsSedeDTO_WithHttpStatusOK() {
        // Given
        Long id = 1L;
        BDDMockito.given(sedeService.findById(id)).willReturn(sede);
        BDDMockito.given(sedeMapper.toDTO(sede)).willReturn(sedeDTO);

        // When
        ResponseEntity<SedeDTO> response = sedeController.findById(id);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(sedeDTO.getNombre(), response.getBody().getNombre());
        Assertions.assertEquals(sedeDTO.getDescripcion(), response.getBody().getDescripcion());

        BDDMockito.then(sedeService).should().findById(id);
        BDDMockito.then(sedeMapper).should().toDTO(sede);
    }

    @Test
    void testSave_ReturnsCreatedStatusAndSedeDTO() {
        // Given
        BDDMockito.given(sedeMapper.toEntity(sedeDTO)).willReturn(sede);
        BDDMockito.given(sedeService.save(sede)).willReturn(sede);
        BDDMockito.given(sedeMapper.toDTO(sede)).willReturn(sedeDTO);

        // When
        ResponseEntity<SedeDTO> response = sedeController.save(sedeDTO);

        // Then
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(sedeDTO.getNombre(), response.getBody().getNombre());

        BDDMockito.then(sedeMapper).should().toEntity(sedeDTO);
        BDDMockito.then(sedeService).should().save(sede);
        BDDMockito.then(sedeMapper).should().toDTO(sede);
    }

    @Test
    void testUpdate_ReturnsUpdatedSedeDTO_WithHttpStatusOK() {
        // Given
        Long id = 1L;
        sedeDTO.setIdSede(id);
        sedeDTO.setNombre("Sede Principal Actualizada");
        sedeDTO.setDescripcion("Descripción actualizada");

        BDDMockito.given(sedeMapper.toEntity(sedeDTO)).willReturn(sede);
        BDDMockito.given(sedeService.update(id, sede)).willReturn(sede);
        BDDMockito.given(sedeMapper.toDTO(sede)).willReturn(sedeDTO);

        // When
        ResponseEntity<SedeDTO> response = sedeController.update(id, sedeDTO);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(sedeDTO.getNombre(), response.getBody().getNombre());

        BDDMockito.then(sedeMapper).should().toEntity(sedeDTO);
        BDDMockito.then(sedeService).should().update(id, sede);
        BDDMockito.then(sedeMapper).should().toDTO(sede);
    }

    @Test
    void testDelete_ReturnsCustomResponse_WithHttpStatusOK() {
        // Given
        Long id = 1L;
        CustomResponse customResponse = new CustomResponse(
                200,
                LocalDateTime.now(),
                "true",
                "Sede eliminada correctamente"
        );

        BDDMockito.given(sedeService.delete(id)).willReturn(customResponse);

        // When
        ResponseEntity<CustomResponse> response = sedeController.delete(id);

        // Then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("true", response.getBody().getMessage());

        BDDMockito.then(sedeService).should().delete(id);
    }
}