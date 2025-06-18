package com.tarea3.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NivelTest {
    @Test
    void calcularNivelBronce() {
        assertEquals(Nivel.BRONCE, Nivel.calcularNivel(0));
        assertEquals(Nivel.BRONCE, Nivel.calcularNivel(250));
        assertEquals(Nivel.BRONCE, Nivel.calcularNivel(499));
    }
    
    @Test
    void calcularNivelPlata() {
        assertEquals(Nivel.PLATA, Nivel.calcularNivel(500));
        assertEquals(Nivel.PLATA, Nivel.calcularNivel(1000));
        assertEquals(Nivel.PLATA, Nivel.calcularNivel(1499));
    }
    
    @Test
    void calcularNivelOro() {
        assertEquals(Nivel.ORO, Nivel.calcularNivel(1500));
        assertEquals(Nivel.ORO, Nivel.calcularNivel(2000));
        assertEquals(Nivel.ORO, Nivel.calcularNivel(2999));
    }
    
    @Test
    void calcularNivelPlatino() {
        assertEquals(Nivel.PLATINO, Nivel.calcularNivel(3000));
        assertEquals(Nivel.PLATINO, Nivel.calcularNivel(5000));
        assertEquals(Nivel.PLATINO, Nivel.calcularNivel(10000));
    }
    
    @Test
    void verificarMultiplicadores() {
        assertEquals(1.0, Nivel.BRONCE.getMultiplicador());
        assertEquals(1.2, Nivel.PLATA.getMultiplicador());
        assertEquals(1.5, Nivel.ORO.getMultiplicador());
        assertEquals(2.0, Nivel.PLATINO.getMultiplicador());
    }
}