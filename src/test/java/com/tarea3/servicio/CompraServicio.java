package com.tarea3.servicio;

import com.tarea3.modelo.Cliente;
import com.tarea3.modelo.Compra;
import com.tarea3.repositorio.CompraRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CompraServicioTest {
    
    private CompraServicio compraServicio;
    private CompraRepositorio compraRepositorio;
    private Cliente clienteBronce;
    private Cliente clientePlata;
    private Cliente clienteOro;
    private Cliente clientePlatino;
    
    @BeforeEach
    void setUp() {
        compraRepositorio = new CompraRepositorio();
        compraServicio = new CompraServicio(compraRepositorio);
        
        clienteBronce = new Cliente(1, "Cliente Bronce", "bronce@test.com");
        
        clientePlata = new Cliente(2, "Cliente Plata", "plata@test.com");
        clientePlata.agregarPuntos(500); // Nivel Plata
        
        clienteOro = new Cliente(3, "Cliente Oro", "oro@test.com");
        clienteOro.agregarPuntos(1500); // Nivel Oro
        
        clientePlatino = new Cliente(4, "Cliente Platino", "platino@test.com");
        clientePlatino.agregarPuntos(3000); // Nivel Platino
    }
    
    @Test
    void calcularPuntosConBonusSinHistorial() {
        Compra compra = new Compra(1, 1, 200, LocalDate.now());
        List<Compra> historial = new ArrayList<>();
        
        int puntos = compraServicio.calcularPuntosConBonus(compra, clienteBronce, historial);
        
        // Sin bonus: 200/100 = 2 puntos * 1.0 = 2
        assertEquals(2, puntos);
    }
    
    @Test
    void calcularPuntosConBonusPrimeraCompraDelDia() {
        LocalDate hoy = LocalDate.now();
        Compra compra = new Compra(1, 1, 200, hoy);
        List<Compra> historial = new ArrayList<>();
        
        int puntos = compraServicio.calcularPuntosConBonus(compra, clienteBronce, historial);
        
        // Primera compra del día, sin bonus
        assertEquals(2, puntos);
    }
    
    @Test
    void calcularPuntosConBonusSegundaCompraDelDia() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 1, 100, hoy);
        Compra compra2 = new Compra(2, 1, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra2, clienteBronce, historial);
        
        // Segunda compra del día, sin bonus aún
        assertEquals(2, puntos);
    }
    
    @Test
    void calcularPuntosConBonusTerceraCompraDelDiaBronce() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 1, 100, hoy);
        Compra compra2 = new Compra(2, 1, 100, hoy);
        Compra compra3 = new Compra(3, 1, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clienteBronce, historial);
        
        // Tercera compra del día con bonus: (2 + 10) * 1.0 = 12
        assertEquals(12, puntos);
    }
    
    @Test
    void calcularPuntosConBonusTerceraCompraDelDiaPlata() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 2, 100, hoy);
        Compra compra2 = new Compra(2, 2, 100, hoy);
        Compra compra3 = new Compra(3, 2, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clientePlata, historial);
        
        // Tercera compra del día con bonus: (2 + 10) * 1.2 = 14.4 -> 14
        assertEquals(14, puntos);
    }
    
    @Test
    void calcularPuntosConBonusTerceraCompraDelDiaOro() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 3, 100, hoy);
        Compra compra2 = new Compra(2, 3, 100, hoy);
        Compra compra3 = new Compra(3, 3, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clienteOro, historial);
        
        // Tercera compra del día con bonus: (2 + 10) * 1.5 = 18
        assertEquals(18, puntos);
    }
    
    @Test
    void calcularPuntosConBonusTerceraCompraDelDiaPlatino() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 4, 100, hoy);
        Compra compra2 = new Compra(2, 4, 100, hoy);
        Compra compra3 = new Compra(3, 4, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clientePlatino, historial);
        
        // Tercera compra del día con bonus: (2 + 10) * 2.0 = 24
        assertEquals(24, puntos);
    }
    
    @Test
    void calcularPuntosConBonusCuartaCompraDelDia() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 1, 100, hoy);
        Compra compra2 = new Compra(2, 1, 100, hoy);
        Compra compra3 = new Compra(3, 1, 100, hoy);
        Compra compra4 = new Compra(4, 1, 200, hoy);
        
        List<Compra> historial = Arrays.asList(compra1, compra2, compra3);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra4, clienteBronce, historial);
        
        // Cuarta compra del día, ya se aplicó bonus, sin bonus: 2 puntos
        assertEquals(2, puntos);
    }
    
    @Test
    void calcularPuntosConBonusComprasDiferentesDias() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        LocalDate hoy = LocalDate.now();
        
        Compra compra1 = new Compra(1, 1, 100, ayer);
        Compra compra2 = new Compra(2, 1, 100, ayer);
        Compra compra3 = new Compra(3, 1, 200, hoy); // Primera del día de hoy
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clienteBronce, historial);
        
        // Primera compra del día de hoy, sin bonus
        assertEquals(2, puntos);
    }
    
    @Test
    void calcularPuntosConBonusComprasDeOtroCliente() {
        LocalDate hoy = LocalDate.now();
        Compra compra1 = new Compra(1, 2, 100, hoy); // Cliente 2
        Compra compra2 = new Compra(2, 2, 100, hoy); // Cliente 2
        Compra compra3 = new Compra(3, 1, 200, hoy); // Cliente 1
        
        List<Compra> historial = Arrays.asList(compra1, compra2);
        
        int puntos = compraServicio.calcularPuntosConBonus(compra3, clienteBronce, historial);
        
        // Primera compra del cliente 1 hoy, sin bonus
        assertEquals(2, puntos);
    }
    
    @Test
    void debeAplicarBonusParametrosNulos() {
        // Nota: Se espera que retorne false (no lanzar excepción) si algún parámetro es nulo.
        assertFalse(compraServicio.debeAplicarBonus(null, clienteBronce, new ArrayList<>()));
        assertFalse(compraServicio.debeAplicarBonus(new Compra(1, 1, 100, LocalDate.now()), null, new ArrayList<>()));
        assertFalse(compraServicio.debeAplicarBonus(new Compra(1, 1, 100, LocalDate.now()), clienteBronce, null));
    }
    
    @Test
    void contarComprasEnFecha() {
        LocalDate hoy = LocalDate.now();
        LocalDate ayer = LocalDate.now().minusDays(1);
        
        List<Compra> historial = Arrays.asList(
            new Compra(1, 1, 100, hoy),
            new Compra(2, 1, 100, hoy),
            new Compra(3, 1, 100, ayer),
            new Compra(4, 2, 100, hoy) // Otro cliente
        );
        
        assertEquals(2, compraServicio.contarComprasEnFecha(clienteBronce, hoy, historial));
        assertEquals(1, compraServicio.contarComprasEnFecha(clienteBronce, ayer, historial));
        assertEquals(0, compraServicio.contarComprasEnFecha(clienteBronce, LocalDate.now().minusDays(2), historial));
    }
    
    @Test
    void contarComprasEnFechaParametrosNulos() {
        assertEquals(0, compraServicio.contarComprasEnFecha(null, LocalDate.now(), new ArrayList<>()));
        assertEquals(0, compraServicio.contarComprasEnFecha(clienteBronce, null, new ArrayList<>()));
        assertEquals(0, compraServicio.contarComprasEnFecha(clienteBronce, LocalDate.now(), null));
    }
    
    @Test
    void calcularPuntosConBonusParametrosNulos() {
        Compra compra = new Compra(1, 1, 100, LocalDate.now());
        List<Compra> historial = new ArrayList<>();
        
        assertThrows(IllegalArgumentException.class, () -> {
            compraServicio.calcularPuntosConBonus(null, clienteBronce, historial);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            compraServicio.calcularPuntosConBonus(compra, null, historial);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            compraServicio.calcularPuntosConBonus(compra, clienteBronce, null);
        });
    }
}