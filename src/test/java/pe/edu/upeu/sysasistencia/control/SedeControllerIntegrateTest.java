package pe.edu.upeu.sysasistencia.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pe.edu.upeu.sysasistencia.dtos.SedeDTO;
import pe.edu.upeu.sysasistencia.dtos.UsuarioDTO;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class SedeControllerIntegrateTest {

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private String token;
    private Long idCreado;

    @BeforeEach
    public void setUp() {
        RestAssured.port = this.port;

        UsuarioDTO.UsuarioCrearDto udto = new UsuarioDTO.UsuarioCrearDto(
                "admin@upeu.edu.pe",
                "Admin123*".toCharArray(),
                "ADMIN",
                "Activo"
        );

        try {
            token = given()
                    .contentType(ContentType.JSON)
                    .body(new UsuarioDTO.CredencialesDto(
                            "admin@upeu.edu.pe",
                            "Admin123*".toCharArray()
                    ))
                    .when().post("/users/login")
                    .andReturn().jsonPath().getString("token");
        } catch (Exception e) {
            if (token == null) {
                token = given()
                        .contentType(ContentType.JSON)
                        .body(udto)
                        .when().post("/users/register")
                        .andReturn().jsonPath().getString("token");
            }
            System.out.println("Token obtenido: " + token);
        }
    }

    @Order(1)
    @Test
    public void testCrearSede() throws Exception {
        SedeDTO dto = new SedeDTO();
        dto.setNombre("Sede Lima Norte");
        dto.setDescripcion("Sede ubicada en Lima Norte");

        SedeDTO response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(objectMapper.writeValueAsString(dto))
                .when()
                .post("/sedes")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("nombre", equalTo("Sede Lima Norte"))
                .extract()
                .as(SedeDTO.class);

        idCreado = response.getIdSede();
        System.out.println("ID de sede creada: " + idCreado);
    }

    @Order(2)
    @Test
    public void testListarSedes() throws Exception {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/sedes")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON);
    }

    @Order(3)
    @Test
    void testFindById() {
        // Obtener la última sede creada
        SedeDTO[] sedes = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/sedes")
                .then()
                .statusCode(200)
                .extract()
                .as(SedeDTO[].class);

        if (sedes.length > 0) {
            Long lastId = sedes[sedes.length - 1].getIdSede();

            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get("/sedes/{id}", lastId)
                    .then()
                    .statusCode(200)
                    .body("idSede", equalTo(lastId.intValue()));
        }
    }

    @Order(4)
    @Test
    void testUpdate() {
        // Obtener la última sede creada
        SedeDTO[] sedes = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/sedes")
                .then()
                .statusCode(200)
                .extract()
                .as(SedeDTO[].class);

        if (sedes.length > 0) {
            Long lastId = sedes[sedes.length - 1].getIdSede();

            SedeDTO updated = new SedeDTO();
            updated.setNombre("Sede Actualizada");
            updated.setDescripcion("Descripción actualizada de la sede");

            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .body(updated)
                    .when()
                    .put("/sedes/{id}", lastId)
                    .then()
                    .statusCode(200)
                    .body("idSede", equalTo(lastId.intValue()))
                    .body("nombre", equalTo("Sede Actualizada"));
        }
    }

    @Order(5)
    @Test
    void testDelete() {
        // Obtener la última sede creada
        SedeDTO[] sedes = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/sedes")
                .then()
                .statusCode(200)
                .extract()
                .as(SedeDTO[].class);

        if (sedes.length > 0) {
            Long lastId = sedes[sedes.length - 1].getIdSede();

            given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/sedes/{id}", lastId)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body("message", equalTo("true"));
        }
    }
}