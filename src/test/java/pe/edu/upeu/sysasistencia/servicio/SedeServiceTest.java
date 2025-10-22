package pe.edu.upeu.sysasistencia.servicio;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upeu.sysasistencia.excepciones.CustomResponse;
import pe.edu.upeu.sysasistencia.excepciones.ModelNotFoundException;
import pe.edu.upeu.sysasistencia.modelo.Sede;
import pe.edu.upeu.sysasistencia.repositorio.ISedeRepository;
import pe.edu.upeu.sysasistencia.servicio.impl.SedeServiceImp;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SedeServiceTest {

    @Mock
    private ISedeRepository repo;

    @InjectMocks
    private SedeServiceImp sedeService;

    private Sede sede;

    @BeforeEach
    public void setUp() {
        // Preparar Sede
        sede = Sede.builder()
                .idSede(1L)
                .nombre("Sede Principal")
                .descripcion("Sede principal de la universidad")
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Guardar Sede")
    public void testSaveSede() {
        // Given
        given(repo.save(sede)).willReturn(sede);

        // When
        Sede guardada = sedeService.save(sede);

        // Then
        Assertions.assertThat(guardada).isNotNull();
        Assertions.assertThat(guardada.getNombre()).isEqualTo(sede.getNombre());
        Assertions.assertThat(guardada.getDescripcion()).isEqualTo("Sede principal de la universidad");
    }

    @Test
    @Order(2)
    @DisplayName("Listar Sedes")
    public void testListSedes() {
        // Given
        Sede sede2 = Sede.builder()
                .idSede(2L)
                .nombre("Sede Lima Norte")
                .descripcion("Sede ubicada en Lima Norte")
                .build();

        given(repo.findAll()).willReturn(List.of(sede, sede2));

        // When
        List<Sede> sedes = sedeService.findAll();

        // Then
        Assertions.assertThat(sedes).hasSize(2);
        Assertions.assertThat(sedes.get(0).getNombre()).isEqualTo("Sede Principal");
        Assertions.assertThat(sedes.get(1).getNombre()).isEqualTo("Sede Lima Norte");

        // Imprimir resultados
        for (Sede s : sedes) {
            System.out.println(s.getNombre() + " - " + s.getDescripcion());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Buscar Sede por ID")
    public void testFindById() {
        // Given
        given(repo.findById(1L)).willReturn(Optional.of(sede));

        // When
        Sede encontrada = sedeService.findById(1L);

        // Then
        Assertions.assertThat(encontrada).isNotNull();
        Assertions.assertThat(encontrada.getIdSede()).isEqualTo(1L);
        Assertions.assertThat(encontrada.getNombre()).isEqualTo("Sede Principal");
    }

    @Test
    @Order(4)
    @DisplayName("Buscar Sede por Nombre")
    public void testFindByNombre() {
        // Given
        given(repo.findByNombre("Sede Principal")).willReturn(Optional.of(sede));

        // When
        Optional<Sede> encontrada = sedeService.findByNombre("Sede Principal");

        // Then
        Assertions.assertThat(encontrada).isPresent();
        Assertions.assertThat(encontrada.get().getNombre()).isEqualTo("Sede Principal");
    }

    @Test
    @Order(5)
    @DisplayName("Actualizar Sede")
    public void testUpdateSede() {
        // Given
        given(repo.findById(1L)).willReturn(Optional.of(sede));
        given(repo.save(sede)).willReturn(sede);

        // When
        sede.setNombre("Sede Principal Actualizada");
        sede.setDescripcion("Descripción actualizada de la sede principal");
        Sede actualizada = sedeService.update(1L, sede);

        // Then
        System.out.println("Nombre actualizado: " + actualizada.getNombre());
        Assertions.assertThat(actualizada.getNombre()).isEqualTo("Sede Principal Actualizada");
        Assertions.assertThat(actualizada.getDescripcion()).isEqualTo("Descripción actualizada de la sede principal");
    }

    @Test
    @Order(6)
    @DisplayName("Eliminar Sede")
    public void testDeleteSede() {
        // Given
        given(repo.findById(1L)).willReturn(Optional.of(sede));
        willDoNothing().given(repo).deleteById(1L);

        // When
        CustomResponse respuesta = sedeService.delete(1L);

        // Then
        System.out.println("Mensaje: " + respuesta.getMessage());
        Assertions.assertThat(respuesta.getMessage()).isEqualTo("true");
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    @Order(7)
    @DisplayName("Eliminar Sede - ID no Existe")
    public void testDeleteByIdNonExistent() {
        // Given
        Long idInexistente = 99L;
        given(repo.findById(idInexistente)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> sedeService.delete(idInexistente))
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessageContaining("ID NOT FOUND: " + idInexistente);
    }

    @Test
    @Order(8)
    @DisplayName("Buscar Sede por ID - No Existe")
    public void testFindByIdNonExistent() {
        // Given
        Long idInexistente = 99L;
        given(repo.findById(idInexistente)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> sedeService.findById(idInexistente))
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessageContaining("ID NOT FOUND: " + idInexistente);
    }

    @Test
    @Order(9)
    @DisplayName("Actualizar Sede - ID no Existe")
    public void testUpdateSedeNonExistent() {
        // Given
        Long idInexistente = 99L;
        given(repo.findById(idInexistente)).willReturn(Optional.empty());

        // When & Then
        Assertions.assertThatThrownBy(() -> sedeService.update(idInexistente, sede))
                .isInstanceOf(ModelNotFoundException.class)
                .hasMessageContaining("ID NOT FOUND: " + idInexistente);
    }

    @Test
    @Order(10)
    @DisplayName("Buscar Sede por Nombre - No Existe")
    public void testFindByNombreNonExistent() {
        // Given
        given(repo.findByNombre("Sede Inexistente")).willReturn(Optional.empty());

        // When
        Optional<Sede> encontrada = sedeService.findByNombre("Sede Inexistente");

        // Then
        Assertions.assertThat(encontrada).isEmpty();
    }
}