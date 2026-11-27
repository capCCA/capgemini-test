package com.capgemini.test.sala;
import org.junit.jupiter.api.Test;

import com.capgemini.sala.SalaDto;

import org.junit.jupiter.api.Assertions;

public class SalaDtoTest {
    
    @Test
    public void testSalaId() {
        SalaDto salaDto = new SalaDto();
        salaDto.setId(1L);
        salaDto.setName("Sala 1");
        Assertions.assertEquals(Long.valueOf(1L), Long.valueOf(salaDto.getId()), "El ID de la sala no coincide");
        Assertions.assertEquals("Sala 1", salaDto.getName(), "El nombre de la sala no coincide");
    }
}
    