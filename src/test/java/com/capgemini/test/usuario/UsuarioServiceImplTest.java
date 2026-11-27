package com.capgemini.test.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.capgemini.usuario.UsuarioRepository;
import com.capgemini.usuario.UsuarioServiceImpl;
import com.capgemini.sala.Sala;
import com.capgemini.sala.SalaDto;
import com.capgemini.sala.SalaRepository;
import com.capgemini.usuario.Usuario;
import com.capgemini.usuario.UsuarioDto;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    public void saveUsuarioOK() {
        Long id = Long.valueOf(5L);
        Long salaId = Long.valueOf(5L);

        Usuario usuarioTest = new Usuario();
        usuarioTest.setId(id);
        usuarioTest.setName("Nombre2");
        usuarioTest.setEmail("aa2@bb.com");
        usuarioTest.setPhone("112233442");
        usuarioTest.setRol("admin");
        usuarioTest.setDni("12345679A");
        usuarioTest.setSala(new Sala());
        usuarioTest.getSala().setId(salaId);

        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(salaId);

        when(salaRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(new Sala()) );
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);
        
        try{
            Long nuevoId = usuarioService.saveUsuario(usuarioDto);

            assertNotNull(nuevoId);
            assertEquals(id, nuevoId);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void saveUsuarioNoSalaKO() {
        try {
            usuarioService.saveUsuario(new UsuarioDto());
            fail();
        } catch (Exception e) {
            assertEquals("El usuario debe indicar una sala", e.getMessage());
        }
    }

    @Test
    public void saveUsuarioSalaNoEncontradaKO() {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setSala(new SalaDto());
        usuarioDto.getSala().setId(7L);
        when(salaRepository.findById(any(Long.class))).thenReturn(java.util.Optional.empty());  
        
        try {
            usuarioService.saveUsuario(usuarioDto);
            fail();
        } catch (Exception e) {
            assertEquals("Sala no encontrada con id 7", e.getMessage());
        }
    }

    @Test
    public void existeUsuarioByEmailOK() {

        String email = "a@b.com";

        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(true);
        
        try{
            boolean exists = usuarioService.existeUsuarioByEmail(email);

            assertNotNull(exists);
            assertEquals(true, exists);
        } catch (Exception e) {
            fail(e.getMessage());
        }   

    }

    @Test
    public void existeUsuarioByEmailKO() {

        String email = "a2@b.com";

        when(usuarioRepository.existsByEmail(any(String.class))).thenReturn(false);
        
        try{
            boolean exists = usuarioService.existeUsuarioByEmail(email);

            assertNotNull(exists);
            assertEquals(false, exists);
        } catch (Exception e) {
            fail();
        }   

    }

    @Test
    public void getUsuarioByIdYSalaOK() {
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

        when(usuarioRepository.findByIdAndSala_Id(any(Long.class), any(Long.class))).thenReturn(java.util.Optional.of(usuarioTest));
        
        try{
            Usuario usuario = usuarioService.getUsuarioByIdYSala(id, salaId);

            assertEquals(id, usuario.getId());
            assertEquals(salaId, usuario.getSala().getId());
            assertEquals("Nombre", usuario.getName());
            assertEquals("aa@bb.com", usuario.getEmail());
            assertEquals("112233445", usuario.getPhone());
            assertEquals("superadmin", usuario.getRol());
            assertEquals("12345678A", usuario.getDni());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void getUsuarioByIdYSalaKO() {
        Long id = Long.valueOf(3L);
        Long salaId = Long.valueOf(3L);

        when(usuarioRepository.findByIdAndSala_Id(any(Long.class), any(Long.class))).thenReturn(java.util.Optional.empty());

        try{
            usuarioService.getUsuarioByIdYSala(id, salaId);
            fail();
        } catch (Exception e) {
            assertEquals("Usuario no encontrado con id 3 para la sala 3", e.getMessage()); 
        }
    }
}
