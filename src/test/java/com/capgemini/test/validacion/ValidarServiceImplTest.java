package com.capgemini.test.validacion;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.mock.MockService;
import com.capgemini.usuario.UsuarioService;
import com.capgemini.validacion.ValidarServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ValidarServiceImplTest {
    
    @Mock
    private MockService mockService;

    @Mock 
    private UsuarioService usuarioService;

    @InjectMocks
    private ValidarServiceImpl validarService;

    @Test 
    public void testValidarUserNameOK() throws Exception {
        // GIVEN
        String name = "TestTe";

        // WHEN
        // THEN
        boolean respuesta = validarService.validarUserName(name);

        // THEN
        assertTrue(respuesta);
    
    }

    @Test 
    public void testValidarUserNameInvalidoOK() throws Exception {
        // GIVEN
        String name = "TestTes";

        // WHEN
        // THEN
        boolean respuesta = validarService.validarUserName(name);

        // THEN
        assertFalse(respuesta);


        // GIVEN
        String name2 = null;

        // WHEN
        boolean respuesta2 = validarService.validarUserName(name2);

        // THEN
        assertFalse(respuesta2);


        // GIVEN
        String name3 = "";

        // WHEN
        boolean respuesta3 = validarService.validarUserName(name3);

        // THEN
        assertFalse(respuesta3);
    }

    @Test 
    public void testValidarEmailOK() throws Exception {
        // GIVEN
        String email = "dd@bb.com";

        // WHEN
        boolean respuesta = validarService.validarEmail(email);

        // THEN
        assertTrue(respuesta);
    }

    @Test 
    public void testValidarEmailInvalidoOK() throws Exception {
        // GIVEN
        String email = "ddbbcom";

        // WHEN
        boolean respuesta = validarService.validarEmail(email);

        // THEN
        assertFalse(respuesta);

        
        // GIVEN
        String email2 = "dd@bbcom";

        // WHEN
        boolean respuesta2 = validarService.validarEmail(email2);

        // THEN
        assertFalse(respuesta2);


        // GIVEN
        String email3 = "ddbb.com";

        // WHEN
        boolean respuesta3 = validarService.validarEmail(email3);

        // THEN
        assertFalse(respuesta3);

    }

    @Test 
    public void testValidarDNIOK() throws Exception {
        // GIVEN
        String dni = null;
        
        // WHEN
        boolean respuesta = validarService.validarDNI(dni);

        // THEN
        assertFalse(respuesta);
    }

    @Test 
    public void testValidarDNIInvalidoOK() throws Exception {
        // GIVEN
        String dni = null;
        
        // WHEN
        boolean respuesta = validarService.validarDNI(dni);

        // THEN
        assertFalse(respuesta);
        
        
        // GIVEN
        String dni2 = "";
        
        // WHEN
        boolean respuesta2 = validarService.validarDNI(dni2);

        // THEN
        assertFalse(respuesta2);

    }

    @Test 
    public void testValidarEmailExistenteOK() throws Exception {
        // GIVEN
        String email = "dd@bb.com";

        when(usuarioService.existeUsuarioByEmail(any(String.class))).thenReturn(true);
        
        // WHEN
        boolean respuesta = validarService.validarEmail(email);

        // THEN
        assertFalse(respuesta);
        
    }

    @Test 
    public void testValidarDNIInvalidoMockOK() throws Exception {
        // GIVEN
        String dni = "123456789A";
        
        when(mockService.checkDni(any(String.class))).thenReturn(false);
        
        // WHEN
        boolean respuesta = validarService.validarDNI(dni);

        // THEN
        assertFalse(respuesta);

    }

    @Test 
    public void testValidarDNIInvalidoErrorMockKO() throws Exception {
        // GIVEN
        String dni = "123456789A";
        
        when(mockService.checkDni(any(String.class))).thenThrow(new RestClientException("Error de mock"));

        // WHEN
        // THEN
        assertThrows(ErrorInternoException.class,
            () -> validarService.validarDNI(dni));

    }
}
