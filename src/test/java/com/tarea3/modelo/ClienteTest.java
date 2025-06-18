package com.tarea3.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {
    @Test
    void crearClienteValido() {
        Cliente cliente = new Cliente(1, "Myckoll Winchester", "myckoll.winchester@usm.cl");

        assertEquals(1, cliente.getId());
        assertEquals("Myckoll Winchester", cliente.getNombre());
        assertEquals("myckoll.winchester@usm.cl", cliente.getCorreo());
        assertEquals(0, cliente.getPuntos());
        assertEquals(Nivel.BRONCE, cliente.getNivel());
        assertEquals(0, cliente.getStreakDias());
    }

    @Test
    void emailInvalidoDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            // sin @
            new Cliente(1, "Mario Romero", "mario.romeron.usm.cl");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            // sin dominio
            new Cliente(1, "Mario Romero", "mario.romeron@");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            // sin usuario
            new Cliente(1, "Mario Romero", "@usm.cl");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            // con doble+ punto
            new Cliente(1, "Mario Romero", "mario..romeron@usm.cl");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            // con espacios
            new Cliente(1, "Mario Romero", "mario romeron@usm.cl");
        });
    }

    @Test
    void nombreVacioDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cliente(1, "", "gabriel.apablaza@usm.cl");
        });
    }

    @Test
    void actualizarPuntosDeberiaRecalcularNivel() {
        Cliente cliente = new Cliente(1, "Joaquín Véliz", "joaquin.velizc@usm.cl");

        // Inicialmente Bronce
        assertEquals(Nivel.BRONCE, cliente.getNivel());

        // Subir a Plata
        cliente.agregarPuntos(500);
        assertEquals(500, cliente.getPuntos());
        assertEquals(Nivel.PLATA, cliente.getNivel());

        // Subir a Oro
        cliente.agregarPuntos(1000); // 1500 total
        assertEquals(1500, cliente.getPuntos());
        assertEquals(Nivel.ORO, cliente.getNivel());

        // Subir a Platino
        cliente.agregarPuntos(1500); // 3000 total
        assertEquals(3000, cliente.getPuntos());
        assertEquals(Nivel.PLATINO, cliente.getNivel());
    }
}