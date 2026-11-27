package com.capgemini.test.usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.excepciones.UsuarioNoEncontradoException;
import com.capgemini.excepciones.ValorIncorrectoException;
import com.capgemini.notificacion.NotificadorService;
import com.capgemini.response.IdResponse;
import com.capgemini.response.UsuarioResponse;
import com.capgemini.usuario.Usuario;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.junit.jupiter.api.extension.ExtendWith;

import com.capgemini.sala.Sala;
import com.capgemini.sala.SalaDto;
import com.capgemini.usuario.UsuarioController;
import com.capgemini.usuario.UsuarioDto;
import com.capgemini.usuario.UsuarioService;
import com.capgemini.validacion.ValidarService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {
    
    @Mock 
    private UsuarioService usuarioService;

    @Mock
    private ValidarService validarService;

    @Mock
    private NotificadorService notificadorService;
    
    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    public void testCrearUsuarioOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("admin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doNothing().when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());
    }

    @Test
    public void testCrearUsuarioAdminOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("admin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doNothing().when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

    }

    @Test
    public void testCrearUsuarioSuperAdminOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("superadmin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doNothing().when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

    }

    @Test
    public void testCrearUsuarioSinSalaOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre");
        usuarioTest.setEmail("aa@bb.com");
        usuarioTest.setPhone("112233445");
        usuarioTest.setRol("superadmin");
        usuarioTest.setDni("12345678A");

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("admin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doNothing().when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

        ArgumentCaptor<UsuarioDto> captor = ArgumentCaptor.forClass(UsuarioDto.class);
        verify(usuarioService).saveUsuario(captor.capture());
        assertTrue(captor.getValue().getSala().getId() == 1);
    }

    @Test
    public void testCrearUsuarioCorregirSalaOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre");
        usuarioTest.setEmail("aa@bb.com");
        usuarioTest.setPhone("112233445");
        usuarioTest.setRol("superadmin");
        usuarioTest.setDni("12345678A");

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("admin");
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(2);

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doNothing().when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

        ArgumentCaptor<UsuarioDto> captor = ArgumentCaptor.forClass(UsuarioDto.class);
        verify(usuarioService).saveUsuario(captor.capture());
        assertTrue(captor.getValue().getSala().getId() == 1);
    }

    @Test 
    public void testCrearUsuarioNombreInvalidoKO() throws Exception {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("TestTes");

        when(validarService.validarUserName(any(String.class))).thenReturn(false);

        // WHEN
        // THEN
        assertThrows(ValorIncorrectoException.class,
            () -> usuarioController.crearUsuario(usuarioDto), "error validation userName");

        // Nunca guardamos
        verify(usuarioService, never()).saveUsuario(any(UsuarioDto.class));
    }

    @Test 
    public void testCrearUsuarioEmailInvalidoKO() throws Exception {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setDni("12345687A");
        usuarioDto.setEmail("ddbbcom");
        usuarioDto.setRol("admin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(false);

        // WHEN
        // THEN
        assertThrows(ValorIncorrectoException.class,
            () -> usuarioController.crearUsuario(usuarioDto), "error validation email");

    }

    @Test 
    public void testCrearUsuarioDniInvalidoKO() throws Exception {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setDni("12345687A");
        
        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(false);

        // WHEN
        // THEN
        assertThrows(ValorIncorrectoException.class,
            () -> usuarioController.crearUsuario(usuarioDto), "error validation dni");

        // Nunca guardamos
        verify(usuarioService, never()).saveUsuario(any(UsuarioDto.class));
        
    }

    @Test
    public void testCrearUsuarioRolKO() throws Exception {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("user");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(false);

        // WHEN
        // THEN
        assertThrows(ValorIncorrectoException.class,
            () -> usuarioController.crearUsuario(usuarioDto), "error validation rol");

        // Nunca guardamos
        verify(usuarioService, never()).saveUsuario(any(UsuarioDto.class));  
    }

    @Test 
    public void testCrearUsuarioExcepcionKO() throws Exception {
        // GIVEN
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("dd@bb.com");
        usuarioDto.setDni("123456789A");
        usuarioDto.setRol("admin");
        
        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenThrow(new ErrorInternoException("Error al crear usuario"));

         // WHEN
        // THEN
        assertThrows(ErrorInternoException.class,
            () -> usuarioController.crearUsuario(usuarioDto), "Error al crear usuario");

    }

    @Test
    public void testCrearUsuarioErrorValidacionMockKO() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);
        Long salaId = Long.valueOf(1L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre");
        usuarioTest.setEmail("aa@bb.com");
        usuarioTest.setPhone("112233445");
        usuarioTest.setRol("superadmin");
        usuarioTest.setDni("12345678A");
        usuarioTest.setSala(new Sala());
        usuarioTest.getSala().setId(salaId);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("superadmin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenThrow(new ErrorInternoException("Error"));

        // WHEN
        // THEN
        assertThrows(ErrorInternoException.class,
            () -> usuarioController.crearUsuario(usuarioDto));

        // Nunca guardamos
        verify(usuarioService, never()).saveUsuario(any(UsuarioDto.class));

    }

    @Test
    public void testCrearUsuarioErrorNotificacionMockOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);
        Long salaId = Long.valueOf(1L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre");
        usuarioTest.setEmail("aa@bb.com");
        usuarioTest.setPhone("112233445");
        usuarioTest.setRol("superadmin");
        usuarioTest.setDni("12345678A");
        usuarioTest.setSala(new Sala());
        usuarioTest.getSala().setId(salaId);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setName("Test");
        usuarioDto.setEmail("cc@dd.com");
        usuarioDto.setPhone("123456789");
        usuarioDto.setDni("98765432B");
        usuarioDto.setRol("superadmin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doThrow(new RestClientException("Error de mock")).when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        ResponseEntity<Object> respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());

        // GIVEN
        UsuarioDto usuarioDto2 = new UsuarioDto();
        usuarioDto2.setName("Test2");
        usuarioDto2.setEmail("cc2@dd.com");
        usuarioDto2.setPhone("123456788");
        usuarioDto2.setDni("98765432A");
        usuarioDto2.setRol("admin");

        when(validarService.validarUserName(any(String.class))).thenReturn(true);
        when(validarService.validarDNI(any(String.class))).thenReturn(true);
        when(validarService.validarRol(any(String.class))).thenReturn(true);
        when(validarService.validarEmail(any(String.class))).thenReturn(true);
        when(usuarioService.saveUsuario(any(UsuarioDto.class))).thenReturn(id);
        Mockito.doThrow(new RestClientException("Error de mock")).when(notificadorService).notificar(any(UsuarioDto.class));

        // WHEN
        respuesta = (ResponseEntity<Object>)usuarioController.crearUsuario(usuarioDto2);

        // THEN
        assertEquals(id, ((IdResponse)respuesta.getBody()).getId());
    }

    @Test
    public void testObtenerUsuarioOK() throws Exception {
        // GIVEN
        Long id = Long.valueOf(1L);
        Long salaId = Long.valueOf(2L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre");
        usuarioTest.setEmail("aa@bb.com");
        usuarioTest.setPhone("112233445");
        usuarioTest.setRol("superadmin");
        usuarioTest.setDni("12345678A");
        usuarioTest.setSala(new Sala());
        usuarioTest.getSala().setId(salaId);

        Long nuevoId = Long.valueOf(10L);

        when(usuarioService.getUsuarioByIdYSala(any(Long.class), any(Long.class ))).thenReturn(usuarioTest);

        // WHEN
        ResponseEntity<Object> response = (ResponseEntity<Object>) usuarioController.obtenerUsuario(nuevoId);
        UsuarioResponse usuario = (UsuarioResponse)response.getBody();

        // THEN
        assertEquals(id, usuario.id());
        assertEquals(salaId, usuario.salaId());
        assertEquals("Nombre", usuario.name());
        assertEquals("aa@bb.com", usuario.email());
        assertEquals("112233445", usuario.phone());
        assertEquals("superadmin", usuario.rol());
        assertEquals("12345678A", usuario.dni());

        // Comprobamos que buscamos por sala 1, devuelva lo que devuelva
        verify(usuarioService).getUsuarioByIdYSala(10L, 1L);
    }

    @Test
    public void testObtenerUsuarioNoExisteKO() throws Exception {
        // GIVEN
        Long nuevoId = Long.valueOf(10L);

        when(usuarioService.getUsuarioByIdYSala(any(Long.class), any(Long.class ))).thenThrow(new UsuarioNoEncontradoException(10L, 10L));

        // WHEN

        // THEN
        assertThrows(UsuarioNoEncontradoException.class,
            () -> usuarioController.obtenerUsuario(nuevoId));

        verify(usuarioService).getUsuarioByIdYSala(10L, 1L);
    }
}
