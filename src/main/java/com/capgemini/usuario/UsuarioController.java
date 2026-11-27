package com.capgemini.usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import com.capgemini.excepciones.ErrorInternoException;
import com.capgemini.excepciones.ValorIncorrectoException;
import com.capgemini.notificacion.NotificadorService;
import com.capgemini.response.IdResponse;
import com.capgemini.response.ResponseUtils;
import com.capgemini.response.UsuarioResponse;
import com.capgemini.sala.SalaDto;
import com.capgemini.validacion.ValidarService;

@RestController
public class UsuarioController {

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ValidarService validarService;

    @Autowired
    private NotificadorService notificadorService;

    private static final Long SALA_POR_DEFECTO = 1L;

    private String validarUsuario(UsuarioDto usuarioDto) throws ErrorInternoException
    {
        String resultado = "OK";
        if (!validarService.validarUserName(usuarioDto.getName())) {
            resultado = "error validation userName";
        }
        else if (!validarService.validarDNI(usuarioDto.getDni())) {
            resultado = "error validation dni";
        }
        else if (!validarService.validarRol(usuarioDto.getRol())) {
            resultado = "error validation rol";
        }
        else if (!validarService.validarEmail(usuarioDto.getEmail())) {
            resultado = "error validation email";
        }
        // Se podria validar que el dni y el phone sea unico segun la bd
        // pero el enunciado no lo pide (el phone no tendria tanto sentido)
        // (tambien que segun la bd el phone es not null)
        return resultado;
    }

    @RequestMapping(path = "/crearUsuario", method = RequestMethod.POST)
    public ResponseEntity<Object> crearUsuario(@RequestBody UsuarioDto usuarioDto)
    {
        // Lo metemos en sala 1 tenga o no
        if (usuarioDto.getSala() == null) {
            usuarioDto.setSala(new SalaDto());
        }
        usuarioDto.getSala().setId(SALA_POR_DEFECTO);
        String respuestaValidacion = validarUsuario(usuarioDto);
        if ("OK".equals(respuestaValidacion)) {
              return guardarUsuario(usuarioDto);
        } else {
            throw new ValorIncorrectoException(respuestaValidacion);
        }
    }

    private ResponseEntity<Object> guardarUsuario(UsuarioDto usuarioDto) throws ValorIncorrectoException, ErrorInternoException {
        boolean usuarioCreado = false;
        Long id = null;
        try {      
            id = usuarioService.saveUsuario(usuarioDto);
            usuarioCreado = true;
            // Por la definicion del mock, asumimos que la notificación siempre será exitosa
            notificadorService.notificar(usuarioDto);
        } catch (Exception e) {
            if (!usuarioCreado) {
                throw new ErrorInternoException(e.getMessage());
            } else {
                // Si esto pudiera provocarse, se revisaria
                // si hacerlo todo como transaction o notificar de forma asincrona
                logger.error("Error al notificar al usuario con id: " + id, e);
            }
        }
        logger.info("Usuario creado con id: " + id);
        return ResponseUtils.returnCreateResponse(new IdResponse(id));
    }

    @RequestMapping(path = "/obtenerUsuario/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> obtenerUsuario(@PathVariable("id") Long id) {
        // Buscamos solo si esta en sala 1, si no nada
        Usuario usuario = usuarioService.getUsuarioByIdYSala(id, SALA_POR_DEFECTO);
        return ResponseUtils.returnOKResponse(UsuarioResponse.from(usuario));
    }
}
