package com.capgemini.test.notificacion;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import com.capgemini.notificacion.NotificadorAdmin;
import com.capgemini.notificacion.NotificadorSuperAdmin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.capgemini.mock.MockService;
import com.capgemini.notificacion.NotificadorServiceImpl;
import com.capgemini.usuario.UsuarioDto;

@ExtendWith(MockitoExtension.class)
public class NotificadorServiceImplTest {

    @Mock
    private MockService mockService;

    @Spy 
    @InjectMocks
    private NotificadorAdmin notificadorAdmin;
    
    @Spy
    @InjectMocks
    private NotificadorSuperAdmin notificadorSuperAdmin;

    @InjectMocks
    private NotificadorServiceImpl notificadorService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificadorService, "notificadores", Arrays.asList(notificadorAdmin, notificadorSuperAdmin));
    }

    @Test
    public void testNotificarAdminOK() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("admin");
        usuario.setEmail("aa@bb.com");

        try {
            when(mockService.emailUser(anyString(), anyString())).thenReturn(HttpStatus.OK);  

            // WHEN
            notificadorService.notificar(usuario);

            // THEN
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin).notificar(usuario);
            verify(notificadorSuperAdmin, never()).notificar(usuario);
            verify(mockService).emailUser(eq(usuario.getEmail()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarAdminConflictOK() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("admin");
        usuario.setEmail("aa@bb.com");

        try {
            when(mockService.emailUser(anyString(), anyString())).thenReturn(HttpStatus.CONFLICT);  

            // WHEN
            notificadorService.notificar(usuario);

            // THEN
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin).notificar(usuario);
            verify(notificadorSuperAdmin, never()).notificar(usuario);
            verify(mockService).emailUser(eq(usuario.getEmail()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarAdminKO() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("admin");
        usuario.setEmail("aa@bb.com");

        try {
            when(mockService.emailUser(anyString(), anyString())).thenThrow(new Exception("Error de mock"));

            // WHEN
            // THEN
            assertThrows(Exception.class,
                () -> notificadorService.notificar(usuario), "Error de mock");
            
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin, never()).aplica(usuario);
            verify(notificadorAdmin).notificar(usuario);
            verify(notificadorSuperAdmin, never()).notificar(usuario);
            verify(mockService).emailUser(eq(usuario.getEmail()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarSuperAdminOK() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("superadmin");
        usuario.setPhone("12345678A");

        try {
            when(mockService.sendSmsUser(anyString(), anyString())).thenReturn(HttpStatus.OK);  

            // WHEN
            notificadorService.notificar(usuario);

            // THEN
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin, never()).notificar(usuario);
            verify(notificadorSuperAdmin).notificar(usuario);
            verify(mockService).sendSmsUser(eq(usuario.getPhone()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarSuperAdminConflictOK() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("superadmin");
        usuario.setPhone("12345678A");

        try {
            when(mockService.sendSmsUser(anyString(), anyString())).thenReturn(HttpStatus.CONFLICT);  

            // WHEN
            notificadorService.notificar(usuario);

            // THEN
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin, never()).notificar(usuario);
            verify(notificadorSuperAdmin).notificar(usuario);
            verify(mockService).sendSmsUser(eq(usuario.getPhone()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarSuperAdminKO() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("superadmin");
        usuario.setPhone("12345678A");

        try {
            when(mockService.sendSmsUser(anyString(), anyString())).thenThrow(new Exception("Error de mock"));

            // WHEN
            // THEN
            assertThrows(Exception.class,
                () -> notificadorService.notificar(usuario), "Error de mock");
            
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin, never()).notificar(usuario);
            verify(notificadorSuperAdmin).notificar(usuario);
            verify(mockService).sendSmsUser(eq(usuario.getPhone()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testNotificarUserOK() {
        // GIVEN
        UsuarioDto usuario = new UsuarioDto();
        usuario.setRol("user");

        try {
            // WHEN
            notificadorService.notificar(usuario);

            // THEN
            verify(notificadorAdmin).aplica(usuario);
            verify(notificadorSuperAdmin).aplica(usuario);
            verify(notificadorAdmin, never()).notificar(usuario);
            verify(notificadorSuperAdmin, never()).notificar(usuario);
            verify(mockService, never()).emailUser(eq(usuario.getEmail()), eq("usuario guardado"));
            verify(mockService, never()).sendSmsUser(eq(usuario.getPhone()), eq("usuario guardado"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
