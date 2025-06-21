package com.tarea3.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class CompraTest {
    
    private Cliente clienteBronce;
    private Cliente clientePlata;
    private Cliente clienteOro;
    private Cliente clientePlatino;
    
    @BeforeEach
    void setUp() {
        clienteBronce = new Cliente(1, "Cliente Bronce", "bronce@test.com");
        
        clientePlata = new Cliente(2, "Cliente Plata", "plata@test.com");
        clientePlata.agregarPuntos(500); // Nivel Plata
        
        clienteOro = new Cliente(3, "Cliente Oro", "oro@test.com");
        clienteOro.agregarPuntos(1500); // Nivel Oro
        
        clientePlatino = new Cliente(4, "Cliente Platino", "platino@test.com");
        clientePlatino.agregarPuntos(3000); // Nivel Platino
    }
    
    @Test
    void crearCompraValida() {
        LocalDate fecha = LocalDate.now();
        Compra compra = new Compra(1, 1, 250, fecha);
        
        assertEquals(1, compra.getIdCompra());
        assertEquals(1, compra.getIdCliente());
        assertEquals(250, compra.getMonto());
        assertEquals(fecha, compra.getFecha());
    }
    
    @Test
    void idCompraInvalidoDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(0, 1, 100, LocalDate.now());
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(-1, 1, 100, LocalDate.now());
        });
    }
    
    @Test
    void idClienteInvalidoDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(1, 0, 100, LocalDate.now());
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(1, -1, 100, LocalDate.now());
        });
    }
    
    @Test
    void montoNegativoDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(1, 1, -10, LocalDate.now());
        });
    }
    
    @Test
    void fechaNulaDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(1, 1, 100, null);
        });
    }
    
    @Test
    void fechaFuturaDeberiaLanzarExcepcion() {
        LocalDate fechaFutura = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> {
            new Compra(1, 1, 100, fechaFutura);
        });
    }
    
    @Test
    void calcularPuntosBase() {
        Compra compra1 = new Compra(1, 1, 150, LocalDate.now());
        assertEquals(1, compra1.calcularPuntosBase()); // 150/100 = 1.5 -> 1
        
        Compra compra2 = new Compra(2, 1, 250, LocalDate.now());
        assertEquals(2, compra2.calcularPuntosBase()); // 250/100 = 2.5 -> 2
        
        Compra compra3 = new Compra(3, 1, 99, LocalDate.now());
        assertEquals(0, compra3.calcularPuntosBase()); // 99/100 = 0.99 -> 0
        
        Compra compra4 = new Compra(4, 1, 1000, LocalDate.now());
        assertEquals(10, compra4.calcularPuntosBase()); // 1000/100 = 10
    }
    
    @Test
    void calcularPuntosTotalesClienteBronce() {
        Compra compra = new Compra(1, 1, 250, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clienteBronce);
        
        // 250/100 = 2.5 -> 2 puntos base * 1.0 (Bronce) = 2
        assertEquals(2, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClienteBronceConBonus() {
        Compra compra = new Compra(1, 1, 250, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clienteBronce, true);
        
        // (2 puntos base + 10 bonus) * 1.0 (Bronce) = 12
        assertEquals(12, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClientePlata() {
        Compra compra = new Compra(1, 2, 250, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clientePlata);
        
        // 250/100 = 2.5 -> 2 puntos base * 1.2 (Plata) = 2.4 -> 2
        assertEquals(2, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClientePlataConBonus() {
        Compra compra = new Compra(1, 2, 250, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clientePlata, true);
        
        // (2 puntos base + 10 bonus) * 1.2 (Plata) = 14.4 -> 14
        assertEquals(14, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClienteOro() {
        Compra compra = new Compra(1, 3, 200, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clienteOro);
        
        // 200/100 = 2 puntos base * 1.5 (Oro) = 3
        assertEquals(3, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClienteOroConBonus() {
        Compra compra = new Compra(1, 3, 200, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clienteOro, true);
        
        // (2 puntos base + 10 bonus) * 1.5 (Oro) = 18
        assertEquals(18, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClientePlatino() {
        Compra compra = new Compra(1, 4, 150, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clientePlatino);
        
        // 150/100 = 1.5 -> 1 punto base * 2.0 (Platino) = 2
        assertEquals(2, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClientePlatinoConBonus() {
        Compra compra = new Compra(1, 4, 150, LocalDate.now());
        int puntosTotales = compra.calcularPuntosTotales(clientePlatino, true);
        
        // (1 punto base + 10 bonus) * 2.0 (Platino) = 22
        assertEquals(22, puntosTotales);
    }
    
    @Test
    void calcularPuntosTotalesClienteNuloDeberiaLanzarExcepcion() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.calcularPuntosTotales(null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.calcularPuntosTotales(null, true);
        });
    }
    
    @Test
    void calcularPuntosTotalesClienteIdNoCoincideDeberiaLanzarExcepcion() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.calcularPuntosTotales(clientePlata); // Cliente ID 2, pero compra es ID 1
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.calcularPuntosTotales(clientePlata, true); // Cliente ID 2, pero compra es ID 1
        });
    }
    
    @Test
    void bonusSoloSeAplicaCuandoEsVerdadero() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        
        // Sin bonus
        int sinBonus = compra.calcularPuntosTotales(clienteBronce, false);
        int normalCall = compra.calcularPuntosTotales(clienteBronce);
        assertEquals(sinBonus, normalCall); // Ambos deberían ser iguales
        
        // Con bonus
        int conBonus = compra.calcularPuntosTotales(clienteBronce, true);
        assertTrue(conBonus > sinBonus); // Con bonus debe ser mayor
        
        // 100/100 = 1 punto base
        // Sin bonus: 1 * 1.0 = 1
        // Con bonus: (1 + 10) * 1.0 = 11
        assertEquals(1, sinBonus);
        assertEquals(11, conBonus);
    }
    
    @Test
    void verificarMismoDia() {
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = LocalDate.now().minusDays(1);
        
        Compra compra = new Compra(1, 1, 100, hoy);
        
        assertTrue(compra.esMismoDia(hoy));
        assertFalse(compra.esMismoDia(ayer));
        assertFalse(compra.esMismoDia(null));
    }
    
    @Test
    void actualizarMonto() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        
        compra.setMonto(200);
        assertEquals(200, compra.getMonto());
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.setMonto(-50);
        });
    }
    
    @Test
    void actualizarFecha() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        LocalDate nuevaFecha = LocalDate.now().minusDays(1);
        
        compra.setFecha(nuevaFecha);
        assertEquals(nuevaFecha, compra.getFecha());
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.setFecha(null);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            compra.setFecha(LocalDate.now().plusDays(1));
        });
    }
    
    @Test
    void testEquals() {
        LocalDate fecha = LocalDate.now();
        Compra compra1 = new Compra(1, 1, 100, fecha);
        Compra compra2 = new Compra(1, 2, 200, fecha); // Mismo ID
        Compra compra3 = new Compra(2, 1, 100, fecha); // Diferente ID
        
        assertEquals(compra1, compra2); // Mismo ID de compra
        assertNotEquals(compra1, compra3); // Diferente ID de compra
        assertNotEquals(compra1, null);
        assertNotEquals(compra1, "string");
    }
    
    @Test
    void testHashCode() {
        LocalDate fecha = LocalDate.now();
        Compra compra1 = new Compra(1, 1, 100, fecha);
        Compra compra2 = new Compra(1, 2, 200, fecha);
        
        assertEquals(compra1.hashCode(), compra2.hashCode());
    }
    
    @Test
    void testToString() {
        LocalDate fecha = LocalDate.of(2024, 6, 20);
        Compra compra = new Compra(1, 2, 150, fecha);
        
        String expected = "Compra{idCompra=1, idCliente=2, monto=150, fecha=2024-06-20}";
        assertEquals(expected, compra.toString());
    }
}