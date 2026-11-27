package com.capgemini.test.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.security.SecureRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capgemini.mock.MockService;
import com.capgemini.response.UsuarioResponse;
import com.capgemini.sala.SalaDto;
import com.capgemini.usuario.UsuarioDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerITTest {

    public static final String LOCALHOST = "http://localhost:";

    @SpyBean
    private MockService mockService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "superadmin"})
    public void testCrearUsuarioOK(String rol) {
        // GIVEN
        // Ponemos strings aleatorios para que no haya repetidos
        String nombre = randomString(6);
        String email1 = randomString(4);
        String email2 = randomString(4);
        String phone = randomString(9);
        String dni = randomString(9);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName(nombre);
        usuarioDto.setEmail(email1 + "@" + email2 + ".com");
        usuarioDto.setPhone(phone);
        usuarioDto.setDni(dni);
        usuarioDto.setRol(rol);
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(1L);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        String[] partes = ((String)respuesta.getBody()).split(":");
        assertEquals("{\"id\"", partes[0]);
        assertNotNull(partes[1]);
        int resultadoId = Integer.parseInt(partes[1].trim().replace("}", ""));
        assertEquals(true, resultadoId > 0);

        // Comprobamos que se ha creado
        ParameterizedTypeReference<UsuarioResponse> responseType2 = new ParameterizedTypeReference<UsuarioResponse>(){};

        ResponseEntity<?> respuesta2 = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + resultadoId, HttpMethod.GET, null, responseType2);
            
        UsuarioResponse usuario = (UsuarioResponse) respuesta2.getBody();
        assertNotNull(usuario);
        assertEquals((long) resultadoId, usuario.id());
        assertEquals(nombre, usuario.name());
        assertEquals(email1 + "@" + email2 + ".com", usuario.email());
        assertEquals(phone, usuario.phone());
        assertEquals(dni, usuario.dni());
        assertEquals(rol, usuario.rol());
        assertEquals(1L, usuario.salaId());
    }

    @Test
    public void testCrearAdminErrorNotificacionOK() {
        // GIVEN
        // Ponemos strings aleatorios para que no haya repetidos
        String nombre = randomString(6);
        String email1 = randomString(4);
        String email2 = randomString(4);
        String phone = randomString(9);
        String dni = randomString(9);
        String rol = "admin";

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName(nombre);
        usuarioDto.setEmail(email1 + "@" + email2 + ".com");
        usuarioDto.setPhone(phone);
        usuarioDto.setDni(dni);
        usuarioDto.setRol(rol);
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(1L);

        try {
            doReturn(true).when(mockService).checkDni(anyString());
            doThrow(new Exception("Mock caído")).when(mockService).emailUser(anyString(), anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        String[] partes = ((String)respuesta.getBody()).split(":");
        assertEquals("{\"id\"", partes[0]);
        assertNotNull(partes[1]);
        int resultadoId = Integer.parseInt(partes[1].trim().replace("}", ""));
        assertEquals(true, resultadoId > 0);

        // Comprobamos que se ha creado
        ParameterizedTypeReference<UsuarioResponse> responseType2 = new ParameterizedTypeReference<UsuarioResponse>(){};

        ResponseEntity<?> respuesta2 = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + resultadoId, HttpMethod.GET, null, responseType2);
            
        UsuarioResponse usuario = (UsuarioResponse) respuesta2.getBody();
        assertNotNull(usuario);
        assertEquals((long) resultadoId, usuario.id());
        assertEquals(nombre, usuario.name());
        assertEquals(email1 + "@" + email2 + ".com", usuario.email());
        assertEquals(phone, usuario.phone());
        assertEquals(dni, usuario.dni());
        assertEquals(rol, usuario.rol());
        assertEquals(1L, usuario.salaId());
    }

    @Test
    public void testCrearSuperAdminErrorNotificacionOK() {
        // GIVEN
        // Ponemos strings aleatorios para que no haya repetidos
        String nombre = randomString(6);
        String email1 = randomString(4);
        String email2 = randomString(4);
        String phone = randomString(9);
        String dni = randomString(9);
        String rol = "superadmin";

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName(nombre);
        usuarioDto.setEmail(email1 + "@" + email2 + ".com");
        usuarioDto.setPhone(phone);
        usuarioDto.setDni(dni);
        usuarioDto.setRol(rol);
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(1L);

        // Introducimos un error en el mock al notificar
        try {
            doReturn(true).when(mockService).checkDni(anyString());
            doThrow(new Exception("Mock caído")).when(mockService).sendSmsUser(anyString(), anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        String[] partes = ((String)respuesta.getBody()).split(":");
        assertEquals("{\"id\"", partes[0]);
        assertNotNull(partes[1]);
        int resultadoId = Integer.parseInt(partes[1].trim().replace("}", ""));
        assertEquals(true, resultadoId > 0);

        // Comprobamos que se ha creado
        ParameterizedTypeReference<UsuarioResponse> responseType2 = new ParameterizedTypeReference<UsuarioResponse>(){};

        ResponseEntity<?> respuesta2 = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + resultadoId, HttpMethod.GET, null, responseType2);
            
        UsuarioResponse usuario = (UsuarioResponse) respuesta2.getBody();
        assertNotNull(usuario);
        assertEquals((long) resultadoId, usuario.id());
        assertEquals(nombre, usuario.name());
        assertEquals(email1 + "@" + email2 + ".com", usuario.email());
        assertEquals(phone, usuario.phone());
        assertEquals(dni, usuario.dni());
        assertEquals(rol, usuario.rol());
        assertEquals(1L, usuario.salaId());
    }

    @Test
    public void testCrearUsuarioCorregirSalaOK() {
        // GIVEN
        // Ponemos strings aleatorios para que no haya repetidos
        String nombre = randomString(6);
        String email1 = randomString(4);
        String email2 = randomString(4);
        String phone = randomString(9);
        String dni = randomString(9);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName(nombre);
        usuarioDto.setEmail(email1 + "@" + email2 + ".com");
        usuarioDto.setPhone(phone);
        usuarioDto.setDni(dni);
        usuarioDto.setRol("admin");
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(10L);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        String[] partes = ((String)respuesta.getBody()).split(":");
        assertEquals("{\"id\"", partes[0]);
        assertNotNull(partes[1]);
        int resultadoId = Integer.parseInt(partes[1].trim().replace("}", ""));
        assertEquals(true, resultadoId > 0);

        // Comprobamos que se ha creado
        ParameterizedTypeReference<UsuarioResponse> responseType2 = new ParameterizedTypeReference<UsuarioResponse>(){};

        ResponseEntity<?> respuesta2 = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + resultadoId, HttpMethod.GET, null, responseType2);
            
        UsuarioResponse usuario = (UsuarioResponse) respuesta2.getBody();
        assertNotNull(usuario);

        assertEquals(1L, usuario.salaId());
    }

    @Test 
    public void testCrearUsuarioNombreInvalidoKO() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("TestTes");

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation userName\"}", respuesta.getBody());


        // GIVEN
        UsuarioDto usuarioDto2 = new UsuarioDto();

        // WHEN
        respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto2), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation userName\"}", respuesta.getBody());


        // GIVEN
        UsuarioDto usuarioDto3 = new UsuarioDto();
        usuarioDto3.setName("");

        // WHEN
        respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto3), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation userName\"}", respuesta.getBody());
    }

    @Test 
    public void testCrearUsuarioEmailInvalidoKO() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setDni("12345678A");
        usuarioDto.setRol("admin");
        usuarioDto.setEmail("ddbbcom");

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);
        
        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation email\"}", respuesta.getBody());


        // GIVEN
        UsuarioDto usuarioDto2 = new UsuarioDto();
        usuarioDto2.setName("Test");
        usuarioDto2.setDni("12345678A");
        usuarioDto2.setRol("admin");
        usuarioDto2.setEmail("dd@bbcom");

        // WHEN
        respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto2), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation email\"}", respuesta.getBody());

        // GIVEN
        UsuarioDto usuarioDto3 = new UsuarioDto();
        usuarioDto3.setName("Test");
        usuarioDto3.setDni("12345678A");
        usuarioDto3.setRol("admin");
        usuarioDto3.setEmail("ddbb.com");

        // WHEN
        respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto3), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation email\"}", respuesta.getBody());
    }

    @Test 
    public void testCrearUsuarioEmailExistenteKO() {
        // GIVEN
        // Comprobamos con el email del usuario 1 que esta en BD
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("juan@example.com");
        usuarioDto.setDni("123456789A");
        usuarioDto.setRol("admin");

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation email\"}", respuesta.getBody());    
    }

    @Test 
    public void testCrearUsuarioDniInvalidoKO() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("dd@bb.com");
        
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation dni\"}", respuesta.getBody());


        // GIVEN
        UsuarioDto usuarioDto2 = new UsuarioDto();
        usuarioDto2.setName("Test");
        usuarioDto2.setEmail("dd@bb.com");
        usuarioDto2.setDni("");
        
        // WHEN
        respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto2), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation dni\"}", respuesta.getBody());
    }

    @Test 
    public void testCrearUsuarioDniIncorrectoMockKO() {
        // GIVEN
        // El mock devuelve error de validación con dni 99999999w
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("dd@bb.com");
        usuarioDto.setDni("99999999w");
        
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation dni\"}", respuesta.getBody());
    }

    @Test 
    public void testCrearUsuarioDniErrorMockKO() {
        // GIVEN
        // El mock devuelve error de validación con dni 99999999w
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("dd@bb.com");
        usuarioDto.setDni("99999999w");

        // Introducimos un error en el mock al validar dni
        try {
            doThrow(new RuntimeException("Error de mock")).when(mockService).checkDni(anyString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertEquals("{\"code\":500,\"message\":\"Error de mock\"}", respuesta.getBody());
    }

    @Test 
    public void testCrearUsuarioRolInvalidoKO() {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("dd@bb.com");
        usuarioDto.setDni("12345678A");
        usuarioDto.setRol("user");
        
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/crearUsuario", HttpMethod.POST, new HttpEntity<>(usuarioDto), responseType);

        // THEN
        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("{\"code\":409,\"message\":\"error validation rol\"}", respuesta.getBody());
    }

    @Test
    public void testObtenerUsuarioOK() {
        // GIVEN
        // Consultamos el usuario 1 que sabemos que esta en BD
        Long id = Long.valueOf(1L);

        ParameterizedTypeReference<UsuarioResponse> responseType = new ParameterizedTypeReference<UsuarioResponse>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + id, HttpMethod.GET, null, responseType);

        // THEN
        UsuarioResponse usuario = (UsuarioResponse) respuesta.getBody();
        assertNotNull(usuario);
        assertEquals(id, usuario.id());
        assertEquals(1L, usuario.salaId());
        assertEquals("Juan Pérez", usuario.name());
        assertEquals("juan@example.com", usuario.email());
        assertEquals("789456123", usuario.phone());
        assertEquals("ADMIN", usuario.rol());
        assertEquals("12345678p", usuario.dni());
    }

    @Test
    public void testObtenerUsuarioNoExisteKO() {
        // GIVEN
        Long id = Long.valueOf(-10L);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};


        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + id, HttpMethod.GET, null, responseType);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("{\"code\":404,\"message\":\"Usuario no encontrado con id -10 para la sala 1\"}", respuesta.getBody());
    }

    @Test
    public void testObtenerUsuarioExisteSalaIncorrectaKO() {
        // GIVEN
        // Consultamos el usuario 2 que pertenece a la sala 2 en BD
        Long id = Long.valueOf(2);

        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};

        // WHEN
        ResponseEntity<?> respuesta = restTemplate.exchange(LOCALHOST + port + "/obtenerUsuario/" + id, HttpMethod.GET, null, responseType);

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("{\"code\":404,\"message\":\"Usuario no encontrado con id 2 para la sala 1\"}", respuesta.getBody());
    }
}
