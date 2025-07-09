package com.tarea3.repositorio;

import com.tarea3.modelo.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class ClienteRepositorioTest {
    
    private ClienteRepositorio repositorio;
    private Cliente cliente1;
    private Cliente cliente2;
    
    @BeforeEach
    void setUp() {
        repositorio = new ClienteRepositorio();
        cliente1 = new Cliente(1, "Juan Pérez", "juan@test.com");
        cliente2 = new Cliente(2, "María García", "maria@test.com");
    }
    
    @Test
    void guardarClienteValido() {
        Cliente clienteGuardado = repositorio.guardar(cliente1);
        
        assertEquals(cliente1, clienteGuardado);
        assertEquals(1, repositorio.contar());
        assertTrue(repositorio.existePorId(1));
    }
    
    @Test
    void guardarClienteNuloDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(null);
        });
    }
    
    @Test
    void guardarClienteConIdDuplicadoDeberiaLanzarExcepcion() {
        repositorio.guardar(cliente1);
        Cliente clienteDuplicado = new Cliente(1, "Otro Cliente", "otro@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.guardar(clienteDuplicado);
        });
    }
    
    @Test
    void buscarPorIdExistente() {
        repositorio.guardar(cliente1);
        
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorId(1);
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente1, clienteEncontrado.get());
    }
    
    @Test
    void buscarPorIdNoExistente() {
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorId(999);
        
        assertFalse(clienteEncontrado.isPresent());
    }
    
    @Test
    void buscarPorCorreoExistente() {
        repositorio.guardar(cliente1);
        
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorCorreo("juan@test.com");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente1, clienteEncontrado.get());
    }
    
    @Test
    void buscarPorCorreoNoExistente() {
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorCorreo("noexiste@test.com");
        
        assertFalse(clienteEncontrado.isPresent());
    }
    
    @Test
    void buscarPorCorreoNulo() {
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorCorreo(null);
        
        assertFalse(clienteEncontrado.isPresent());
    }
    
    @Test
    void buscarPorCorreoIgnorandoMayusculas() {
        repositorio.guardar(cliente1);
        
        Optional<Cliente> clienteEncontrado = repositorio.buscarPorCorreo("JUAN@TEST.COM");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente1, clienteEncontrado.get());
    }
    
    @Test
    void obtenerTodos() {
        repositorio.guardar(cliente1);
        repositorio.guardar(cliente2);
        
        List<Cliente> clientes = repositorio.obtenerTodos();
        
        assertEquals(2, clientes.size());
        assertTrue(clientes.contains(cliente1));
        assertTrue(clientes.contains(cliente2));
    }
    
    @Test
    void obtenerTodosRepositorioVacio() {
        List<Cliente> clientes = repositorio.obtenerTodos();
        
        assertTrue(clientes.isEmpty());
    }
    
    @Test
    void actualizarClienteExistente() {
        repositorio.guardar(cliente1);
        cliente1.setNombre("Juan Carlos Pérez");
        
        Cliente clienteActualizado = repositorio.actualizar(cliente1);
        
        assertEquals("Juan Carlos Pérez", clienteActualizado.getNombre());
        assertEquals(cliente1, repositorio.buscarPorId(1).get());
    }
    
    @Test
    void actualizarClienteNuloDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.actualizar(null);
        });
    }
    
    @Test
    void actualizarClienteNoExistenteDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            repositorio.actualizar(cliente1);
        });
    }
    
    @Test
    void eliminarClienteExistente() {
        repositorio.guardar(cliente1);
        
        boolean eliminado = repositorio.eliminar(1);
        
        assertTrue(eliminado);
        assertFalse(repositorio.existePorId(1));
        assertEquals(0, repositorio.contar());
    }
    
    @Test
    void eliminarClienteNoExistente() {
        boolean eliminado = repositorio.eliminar(999);
        
        assertFalse(eliminado);
    }
    
    @Test
    void existePorIdClienteExistente() {
        repositorio.guardar(cliente1);
        
        assertTrue(repositorio.existePorId(1));
    }
    
    @Test
    void existePorIdClienteNoExistente() {
        assertFalse(repositorio.existePorId(999));
    }
    
    @Test
    void existePorCorreoClienteExistente() {
        repositorio.guardar(cliente1);
        
        assertTrue(repositorio.existePorCorreo("juan@test.com"));
    }
    
    @Test
    void existePorCorreoClienteNoExistente() {
        assertFalse(repositorio.existePorCorreo("noexiste@test.com"));
    }
    
    @Test
    void existePorCorreoNulo() {
        assertFalse(repositorio.existePorCorreo(null));
    }
    
    @Test
    void existePorCorreoIgnorandoMayusculas() {
        repositorio.guardar(cliente1);
        
        assertTrue(repositorio.existePorCorreo("JUAN@TEST.COM"));
    }
    
    @Test
    void contarClientes() {
        assertEquals(0, repositorio.contar());
        
        repositorio.guardar(cliente1);
        assertEquals(1, repositorio.contar());
        
        repositorio.guardar(cliente2);
        assertEquals(2, repositorio.contar());
        
        repositorio.eliminar(1);
        assertEquals(1, repositorio.contar());
    }
    
    @Test
    void limpiarRepositorio() {
        repositorio.guardar(cliente1);
        repositorio.guardar(cliente2);
        assertEquals(2, repositorio.contar());
        
        repositorio.limpiar();
        
        assertEquals(0, repositorio.contar());
        assertTrue(repositorio.obtenerTodos().isEmpty());
    }
}
