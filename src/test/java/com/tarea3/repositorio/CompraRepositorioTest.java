package com.tarea3.repositorio;

import com.tarea3.modelo.Compra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class CompraRepositorioTest {
    
    private CompraRepositorio repositorio;
    private Compra compra1;
    private Compra compra2;
    private Compra compra3;
    private LocalDate hoy;
    private LocalDate ayer;
    
    @BeforeEach
    void setUp() {
        repositorio = new CompraRepositorio();
        hoy = LocalDate.now();
        ayer = LocalDate.now().minusDays(1);
        
        compra1 = new Compra(1, 1, 100, hoy);
        compra2 = new Compra(2, 1, 200, ayer);
        compra3 = new Compra(3, 2, 150, hoy);
    }
    
    @Test
    void guardarCompraValida() {
        Compra compraGuardada = repositorio.guardar(compra1);
        
        assertEquals(compra1, compraGuardada);
        assertEquals(1, repositorio.contar());
        assertTrue(repositorio.existePorId(1));
    }
    
    @Test
    void guardarCompraNulaDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(null);
        });
    }
    
    @Test
    void guardarCompraConIdDuplicadoDeberiaLanzarExcepcion() {
        repositorio.guardar(compra1);
        Compra compraDuplicada = new Compra(1, 2, 300, hoy);
        
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(compraDuplicada);
        });
    }
    
    @Test
    void buscarPorIdExistente() {
        repositorio.guardar(compra1);
        
        Optional<Compra> compraEncontrada = repositorio.buscarPorId(1);
        
        assertTrue(compraEncontrada.isPresent());
        assertEquals(compra1, compraEncontrada.get());
    }
    
    @Test
    void buscarPorIdNoExistente() {
        Optional<Compra> compraEncontrada = repositorio.buscarPorId(999);
        
        assertFalse(compraEncontrada.isPresent());
    }
    
    @Test
    void obtenerTodas() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        repositorio.guardar(compra3);
        
        List<Compra> compras = repositorio.obtenerTodas();
        
        assertEquals(3, compras.size());
        assertTrue(compras.contains(compra1));
        assertTrue(compras.contains(compra2));
        assertTrue(compras.contains(compra3));
    }
    
    @Test
    void obtenerTodasRepositorioVacio() {
        List<Compra> compras = repositorio.obtenerTodas();
        
        assertTrue(compras.isEmpty());
    }
    
    @Test
    void obtenerPorCliente() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        repositorio.guardar(compra3);
        
        List<Compra> comprasCliente1 = repositorio.obtenerPorCliente(1);
        List<Compra> comprasCliente2 = repositorio.obtenerPorCliente(2);
        
        assertEquals(2, comprasCliente1.size());
        assertTrue(comprasCliente1.contains(compra1));
        assertTrue(comprasCliente1.contains(compra2));
        
        assertEquals(1, comprasCliente2.size());
        assertTrue(comprasCliente2.contains(compra3));
    }
    
    @Test
    void obtenerPorClienteNoExistente() {
        repositorio.guardar(compra1);
        
        List<Compra> compras = repositorio.obtenerPorCliente(999);
        
        assertTrue(compras.isEmpty());
    }
    
    @Test
    void obtenerPorClienteYFecha() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        repositorio.guardar(compra3);
        
        List<Compra> comprasCliente1Hoy = repositorio.obtenerPorClienteYFecha(1, hoy);
        List<Compra> comprasCliente1Ayer = repositorio.obtenerPorClienteYFecha(1, ayer);
        
        assertEquals(1, comprasCliente1Hoy.size());
        assertTrue(comprasCliente1Hoy.contains(compra1));
        
        assertEquals(1, comprasCliente1Ayer.size());
        assertTrue(comprasCliente1Ayer.contains(compra2));
    }
    
    @Test
    void obtenerPorClienteYFechaNula() {
        repositorio.guardar(compra1);
        
        List<Compra> compras = repositorio.obtenerPorClienteYFecha(1, null);
        
        assertTrue(compras.isEmpty());
    }
    
    @Test
    void obtenerPorRangoFechas() {
        LocalDate hace3Dias = LocalDate.now().minusDays(3);
        
        Compra compraAntigua = new Compra(4, 1, 50, hace3Dias);
        repositorio.guardar(compraAntigua);
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        
        List<Compra> comprasRango = repositorio.obtenerPorRangoFechas(ayer, hoy);
        
        assertEquals(2, comprasRango.size());
        assertTrue(comprasRango.contains(compra1));
        assertTrue(comprasRango.contains(compra2));
        assertFalse(comprasRango.contains(compraAntigua));
    }
    
    @Test
    void obtenerPorRangoFechasNulas() {
        repositorio.guardar(compra1);
        
        List<Compra> comprasRango1 = repositorio.obtenerPorRangoFechas(null, hoy);
        List<Compra> comprasRango2 = repositorio.obtenerPorRangoFechas(hoy, null);
        
        assertTrue(comprasRango1.isEmpty());
        assertTrue(comprasRango2.isEmpty());
    }
    
    @Test
    void actualizarCompraExistente() {
        repositorio.guardar(compra1);
        compra1.setMonto(150);
        
        Compra compraActualizada = repositorio.actualizar(compra1);
        
        assertEquals(150, compraActualizada.getMonto());
        assertEquals(compra1, repositorio.buscarPorId(1).get());
    }
    
    @Test
    void actualizarCompraNulaDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.actualizar(null);
        });
    }
    
    @Test
    void actualizarCompraNoExistenteDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.actualizar(compra1);
        });
    }
    
    @Test
    void eliminarCompraExistente() {
        repositorio.guardar(compra1);
        
        boolean eliminado = repositorio.eliminar(1);
        
        assertTrue(eliminado);
        assertFalse(repositorio.existePorId(1));
        assertEquals(0, repositorio.contar());
    }
    
    @Test
    void eliminarCompraNoExistente() {
        boolean eliminado = repositorio.eliminar(999);
        
        assertFalse(eliminado);
    }
    
    @Test
    void eliminarPorCliente() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        repositorio.guardar(compra3);
        
        int eliminadas = repositorio.eliminarPorCliente(1);
        
        assertEquals(2, eliminadas);
        assertEquals(1, repositorio.contar());
        assertTrue(repositorio.existePorId(3));
        assertFalse(repositorio.existePorId(1));
        assertFalse(repositorio.existePorId(2));
    }
    
    @Test
    void eliminarPorClienteNoExistente() {
        repositorio.guardar(compra1);
        
        int eliminadas = repositorio.eliminarPorCliente(999);
        
        assertEquals(0, eliminadas);
        assertEquals(1, repositorio.contar());
    }
    
    @Test
    void existePorIdCompraExistente() {
        repositorio.guardar(compra1);
        
        assertTrue(repositorio.existePorId(1));
    }
    
    @Test
    void existePorIdCompraNoExistente() {
        assertFalse(repositorio.existePorId(999));
    }
    
    @Test
    void contarCompras() {
        assertEquals(0, repositorio.contar());
        
        repositorio.guardar(compra1);
        assertEquals(1, repositorio.contar());
        
        repositorio.guardar(compra2);
        assertEquals(2, repositorio.contar());
        
        repositorio.eliminar(1);
        assertEquals(1, repositorio.contar());
    }
    
    @Test
    void contarPorCliente() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        repositorio.guardar(compra3);
        
        assertEquals(2, repositorio.contarPorCliente(1));
        assertEquals(1, repositorio.contarPorCliente(2));
        assertEquals(0, repositorio.contarPorCliente(999));
    }
    
    @Test
    void limpiarRepositorio() {
        repositorio.guardar(compra1);
        repositorio.guardar(compra2);
        assertEquals(2, repositorio.contar());
        
        repositorio.limpiar();
        
        assertEquals(0, repositorio.contar());
        assertTrue(repositorio.obtenerTodas().isEmpty());
    }
}
