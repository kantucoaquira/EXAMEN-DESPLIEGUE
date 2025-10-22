package pe.edu.upeu.sysasistencia.repositorio;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.upeu.sysasistencia.modelo.Sede;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ISedeRepositoryTest {

    @Autowired
    private ISedeRepository sedeRepository;

    private static Long sedeId;

    @BeforeEach
    public void setUp() {
        // Crear una Sede de prueba
        Sede sede = new Sede();
        sede.setNombre("Sede Principal");
        sede.setDescripcion("Sede principal de la universidad");
        Sede sedeGuardada = sedeRepository.save(sede);
        sedeId = sedeGuardada.getIdSede();
    }

    @Test
    @Order(1)
    public void testGuardarSede() {
        Sede nuevaSede = new Sede();
        nuevaSede.setNombre("Sede Lima Norte");
        nuevaSede.setDescripcion("Sede ubicada en Lima Norte");

        Sede guardada = sedeRepository.save(nuevaSede);

        assertNotNull(guardada.getIdSede());
        assertEquals("Sede Lima Norte", guardada.getNombre());
        assertEquals("Sede ubicada en Lima Norte", guardada.getDescripcion());
    }

    @Test
    @Order(2)
    public void testBuscarPorId() {
        Optional<Sede> sede = sedeRepository.findById(sedeId);

        assertTrue(sede.isPresent());
        assertEquals("Sede Principal", sede.get().getNombre());
        assertEquals("Sede principal de la universidad", sede.get().getDescripcion());
    }

    @Test
    @Order(3)
    public void testBuscarPorNombre() {
        Optional<Sede> sede = sedeRepository.findByNombre("Sede Principal");

        assertTrue(sede.isPresent());
        assertEquals(sedeId, sede.get().getIdSede());
        assertEquals("Sede Principal", sede.get().getNombre());
    }

    @Test
    @Order(4)
    public void testExistsByNombre() {
        boolean existe = sedeRepository.existsByNombre("Sede Principal");
        boolean noExiste = sedeRepository.existsByNombre("Sede Inexistente");

        assertTrue(existe);
        assertFalse(noExiste);
    }

    @Test
    @Order(5)
    public void testActualizarSede() {
        Sede sede = sedeRepository.findById(sedeId).orElseThrow();
        sede.setNombre("Sede Principal Actualizada");
        sede.setDescripcion("Descripción actualizada de la sede principal");

        Sede actualizada = sedeRepository.save(sede);

        assertEquals("Sede Principal Actualizada", actualizada.getNombre());
        assertEquals("Descripción actualizada de la sede principal", actualizada.getDescripcion());
    }

    @Test
    @Order(6)
    public void testListarSedes() {
        List<Sede> sedes = sedeRepository.findAll();

        assertFalse(sedes.isEmpty());
        System.out.println("Total sedes registradas: " + sedes.size());
        for (Sede s : sedes) {
            System.out.println(s.getNombre() + "\t" + s.getIdSede() + "\t" + s.getDescripcion());
        }
    }

    @Test
    @Order(7)
    public void testEliminarSede() {
        sedeRepository.deleteById(sedeId);
        Optional<Sede> eliminada = sedeRepository.findById(sedeId);

        assertFalse(eliminada.isPresent(), "La sede debería haber sido eliminada");
    }

    @Test
    @Order(8)
    public void testBuscarPorNombreNoExistente() {
        Optional<Sede> sede = sedeRepository.findByNombre("Sede Que No Existe");

        assertFalse(sede.isPresent());
    }

    @Test
    @Order(9)
    public void testGuardarSedeConNombreUnico() {
        Sede sede1 = new Sede();
        sede1.setNombre("Sede Única");
        sede1.setDescripcion("Primera sede");
        sedeRepository.save(sede1);

        // Intentar guardar otra sede con el mismo nombre debería fallar
        // debido a la constraint UNIQUE en la columna nombre
        Sede sede2 = new Sede();
        sede2.setNombre("Sede Única");
        sede2.setDescripcion("Segunda sede");

        assertThrows(Exception.class, () -> {
            sedeRepository.save(sede2);
            sedeRepository.flush(); // Forzar la sincronización con la BD
        });
    }

    @Test
    @Order(10)
    public void testGuardarSedeConNombreNulo() {
        Sede sede = new Sede();
        sede.setNombre(null);
        sede.setDescripcion("Sede sin nombre");

        // No se puede guardar una sede sin nombre (nullable = false)
        assertThrows(Exception.class, () -> {
            sedeRepository.save(sede);
            sedeRepository.flush();
        });
    }
}