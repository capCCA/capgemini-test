package com.capgemini.test.sala;
import org.junit.jupiter.api.Test;

import com.capgemini.sala.Sala;

import org.junit.jupiter.api.Assertions;

public class SalaTest {
    
    @Test
    public void testSalaId() {
        Sala sala = new Sala();
        sala.setId(1L);
        sala.setName("Sala 1");
        Assertions.assertEquals(Long.valueOf(1L), Long.valueOf(sala.getId()), "El ID de la sala no coincide");
        Assertions.assertEquals("Sala 1", sala.getName(), "El nombre de la sala no coincide");
    }
}
    