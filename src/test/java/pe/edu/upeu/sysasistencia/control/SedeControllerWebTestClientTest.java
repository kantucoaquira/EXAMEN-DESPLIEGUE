package pe.edu.upeu.sysasistencia.control;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.sysasistencia.dtos.SedeDTO;
import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SedeControllerWebTestClientTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    private String token;
    Logger logger = Logger.getLogger(SedeControllerWebTestClientTest.class.getName());

    SedeDTO sedeDTO;
    Long idx;

    @BeforeEach
    public void setUp() {
        System.out.println("Puerto: " + this.port);

        UsuarioDTO.UsuarioCrearDto udto = new UsuarioDTO.UsuarioCrearDto(
                "admin@upeu.edu.pe",
                "Admin123*".toCharArray(),
                "ADMIN",
                "Activo"
        );

        try {
            var response = webTestClient.post()
                    .uri("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioDTO.CredencialesDto(
                            "admin@upeu.edu.pe",
                            "Admin123*".toCharArray()
                    ))
                    .exchange()
                    .expectBody(String.class)
                    .returnResult()
                    .getResponseBody();

            JSONObject jsonObj = new JSONObject(response);
            if (jsonObj.length() > 1) {
                token = jsonObj.getString("token") != null ? jsonObj.getString("token") : null;
            }
        } catch (JSONException e) {
            System.out.println("Error en login: " + e.getMessage());
            if (token == null) {
                webTestClient.post()
                        .uri("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(udto)
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(String.class)
                        .value(tokenx -> {
                            try {
                                JSONObject jsonObjx = new JSONObject(tokenx);
                                if (jsonObjx.length() > 1) {
                                    token = jsonObjx.getString("token");
                                }
                            } catch (JSONException ex) {
                                logger.log(Level.SEVERE, null, ex);
                            }
                        });
            }
        }
    }

    @Test
    @Order(1)
    public void testListarSedes() {
        System.out.println("Token: " + token);
        webTestClient.get()
                .uri("http://localhost:" + this.port + "/sedes")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$").isArray();
    }

    @Transactional
    @Test
    @Order(2)
    public void testGuardarSede() {
        // Generar nombre único con timestamp
        String nombreUnico = "Sede Filial " + System.currentTimeMillis();

        sedeDTO = new SedeDTO();
        sedeDTO.setNombre(nombreUnico);
        sedeDTO.setDescripcion("Sede filial de prueba");

        try {
            var datoBuscado = webTestClient.post()
                    .uri("http://localhost:" + this.port + "/sedes")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(sedeDTO)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(String.class)
                    .returnResult()
                    .getResponseBody();

            JSONObject jsonObj = new JSONObject(datoBuscado);
            if (jsonObj.has("idSede")) {
                idx = jsonObj.getLong("idSede");
            }

            System.out.println("Sede creada: " + nombreUnico);
        } catch (JSONException e) {
            System.out.println("Error al guardar: " + e);
        }
        System.out.println("ID Creado: " + idx);
    }

    @Transactional
    @Test
    @Order(3)
    public void testActualizarSede() {
        // Obtener el ID de la última sede
        var response = webTestClient.get()
                .uri("http://localhost:" + this.port + "/sedes")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject lastSede = jsonArray.getJSONObject(jsonArray.length() - 1);
                Long lastId = lastSede.getLong("idSede");

                SedeDTO sedeActualizada = new SedeDTO();
                sedeActualizada.setNombre("Sede Actualizada");
                sedeActualizada.setDescripcion("Descripción actualizada de la sede");

                webTestClient.put()
                        .uri("http://localhost:" + this.port + "/sedes/{id}", lastId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(sedeActualizada)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.nombre").isEqualTo("Sede Actualizada");
            }
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Error al actualizar sede", e);
        }
    }

    @Test
    @Order(4)
    public void testBuscarSede() {
        // Obtener el ID de la última sede
        var response = webTestClient.get()
                .uri("http://localhost:" + this.port + "/sedes")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject lastSede = jsonArray.getJSONObject(jsonArray.length() - 1);
                Long lastId = lastSede.getLong("idSede");

                webTestClient.get()
                        .uri("http://localhost:" + this.port + "/sedes/{id}", lastId)
                        .header("Authorization", "Bearer " + token)
                        .exchange()
                        .expectStatus().isOk()
                        .expectHeader().contentType(MediaType.APPLICATION_JSON)
                        .expectBody()
                        .jsonPath("$.idSede").isEqualTo(lastId);
            }
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Error al buscar sede", e);
        }
    }

    @Test
    @Order(5)
    public void testEliminarSede() {
        // Obtener el ID de la última sede
        var response = webTestClient.get()
                .uri("http://localhost:" + this.port + "/sedes")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        try {
            org.json.JSONArray jsonArray = new org.json.JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject lastSede = jsonArray.getJSONObject(jsonArray.length() - 1);
                Long lastId = lastSede.getLong("idSede");

                System.out.println("Eliminar ID: " + lastId);

                webTestClient.delete()
                        .uri("http://localhost:" + this.port + "/sedes/{id}", lastId)
                        .header("Authorization", "Bearer " + token)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.message").isEqualTo("true");
            }
        } catch (JSONException e) {
            logger.log(Level.SEVERE, "Error al eliminar sede", e);
        }
    }
}