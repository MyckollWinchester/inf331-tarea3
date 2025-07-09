package com.tarea3.servicio;

import com.tarea3.modelo.Cliente;
import com.tarea3.modelo.Nivel;
import com.tarea3.repositorio.ClienteRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class ClienteServicioTest {
    
    private ClienteServicio clienteServicio;
    private ClienteRepositorio clienteRepositorio;
    
    @BeforeEach
    void setUp() {
        clienteRepositorio = new ClienteRepositorio();
        clienteServicio = new ClienteServicio(clienteRepositorio);
    }
    
    @Test
    void crearClienteConIdAutoincrementalValido() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        assertNotNull(cliente);
        assertEquals(1, cliente.getId()); // Primer ID autoincremental
        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("juan@test.com", cliente.getCorreo());
    }
    
    @Test
    void crearClienteConIdEspecificoValido() {
        Cliente cliente = clienteServicio.crearCliente(5, "María García", "maria@test.com");
        
        assertNotNull(cliente);
        assertEquals(5, cliente.getId());
        assertEquals("María García", cliente.getNombre());
        assertEquals("maria@test.com", cliente.getCorreo());
    }
    
    @Test
    void crearMultiplesClientesConIdAutoincremental() {
        Cliente cliente1 = clienteServicio.crearCliente("Cliente 1", "cliente1@test.com");
        Cliente cliente2 = clienteServicio.crearCliente("Cliente 2", "cliente2@test.com");
        Cliente cliente3 = clienteServicio.crearCliente("Cliente 3", "cliente3@test.com");
        
        assertEquals(1, cliente1.getId());
        assertEquals(2, cliente2.getId());
        assertEquals(3, cliente3.getId());
    }
    
    @Test
    void crearClienteConCorreoDuplicadoDeberiaLanzarExcepcion() {
        clienteServicio.crearCliente("Cliente 1", "test@email.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.crearCliente("Cliente 2", "test@email.com");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.crearCliente(10, "Cliente 3", "test@email.com");
        });
    }
    
    @Test
    void buscarPorIdExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Optional<Cliente> clienteEncontrado = clienteServicio.buscarPorId(cliente.getId());
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente, clienteEncontrado.get());
    }
    
    @Test
    void buscarPorIdNoExistente() {
        Optional<Cliente> clienteEncontrado = clienteServicio.buscarPorId(999);
        
        assertFalse(clienteEncontrado.isPresent());
    }
    
    @Test
    void buscarPorCorreoExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Optional<Cliente> clienteEncontrado = clienteServicio.buscarPorCorreo("juan@test.com");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente, clienteEncontrado.get());
    }
    
    @Test
    void buscarPorCorreoNoExistente() {
        Optional<Cliente> clienteEncontrado = clienteServicio.buscarPorCorreo("noexiste@test.com");
        
        assertFalse(clienteEncontrado.isPresent());
    }
    
    @Test
    void buscarPorCorreoIgnorandoMayusculas() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Optional<Cliente> clienteEncontrado = clienteServicio.buscarPorCorreo("JUAN@TEST.COM");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals(cliente, clienteEncontrado.get());
    }
    
    @Test
    void obtenerTodos() {
        Cliente cliente1 = clienteServicio.crearCliente("Cliente 1", "cliente1@test.com");
        Cliente cliente2 = clienteServicio.crearCliente("Cliente 2", "cliente2@test.com");
        Cliente cliente3 = clienteServicio.crearCliente("Cliente 3", "cliente3@test.com");
        
        List<Cliente> clientes = clienteServicio.obtenerTodos();
        
        assertEquals(3, clientes.size());
        assertTrue(clientes.contains(cliente1));
        assertTrue(clientes.contains(cliente2));
        assertTrue(clientes.contains(cliente3));
    }
    
    @Test
    void obtenerTodosConRepositorioVacio() {
        List<Cliente> clientes = clienteServicio.obtenerTodos();
        
        assertTrue(clientes.isEmpty());
    }
    
    @Test
    void actualizarNombreClienteExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Cliente clienteActualizado = clienteServicio.actualizarNombre(cliente.getId(), "Juan Carlos Pérez");
        
        assertEquals("Juan Carlos Pérez", clienteActualizado.getNombre());
        assertEquals(cliente.getId(), clienteActualizado.getId());
        assertEquals(cliente.getCorreo(), clienteActualizado.getCorreo());
    }
    
    @Test
    void actualizarNombreClienteNoExistenteDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.actualizarNombre(999, "Nuevo Nombre");
        });
    }
    
    @Test
    void actualizarCorreoClienteExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Cliente clienteActualizado = clienteServicio.actualizarCorreo(cliente.getId(), "juan.perez@test.com");
        
        assertEquals("juan.perez@test.com", clienteActualizado.getCorreo());
        assertEquals(cliente.getId(), clienteActualizado.getId());
        assertEquals(cliente.getNombre(), clienteActualizado.getNombre());
    }
    
    @Test
    void actualizarCorreoClienteNoExistenteDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.actualizarCorreo(999, "nuevo@test.com");
        });
    }
    
    @Test
    void actualizarCorreoConCorreoDuplicadoDeberiaLanzarExcepcion() {
        Cliente cliente1 = clienteServicio.crearCliente("Cliente 1", "cliente1@test.com");
        clienteServicio.crearCliente("Cliente 2", "cliente2@test.com");
        
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.actualizarCorreo(cliente1.getId(), "cliente2@test.com");
        });
    }
    
    @Test
    void actualizarCorreoConMismoCorreoActualDeberiaFuncionar() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        // Actualizar con el mismo correo no debería lanzar excepción
        Cliente clienteActualizado = clienteServicio.actualizarCorreo(cliente.getId(), "juan@test.com");
        
        assertEquals("juan@test.com", clienteActualizado.getCorreo());
    }
    
    @Test
    void agregarPuntosClienteExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        Cliente clienteActualizado = clienteServicio.agregarPuntos(cliente.getId(), 300);
        
        assertEquals(300, clienteActualizado.getPuntos());
        assertEquals(Nivel.BRONCE, clienteActualizado.getNivel());
    }
    
    @Test
    void agregarPuntosClienteNoExistenteDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteServicio.agregarPuntos(999, 100);
        });
    }
    
    @Test
    void eliminarClienteExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        boolean eliminado = clienteServicio.eliminarCliente(cliente.getId());
        
        assertTrue(eliminado);
        assertFalse(clienteServicio.existeCliente(cliente.getId()));
    }
    
    @Test
    void eliminarClienteNoExistente() {
        boolean eliminado = clienteServicio.eliminarCliente(999);
        
        assertFalse(eliminado);
    }
    
    @Test
    void existeClienteExistente() {
        Cliente cliente = clienteServicio.crearCliente("Juan Pérez", "juan@test.com");
        
        assertTrue(clienteServicio.existeCliente(cliente.getId()));
    }
    
    @Test
    void existeClienteNoExistente() {
        assertFalse(clienteServicio.existeCliente(999));
    }
    
    @Test
    void contarClientes() {
        assertEquals(0, clienteServicio.contarClientes());
        
        clienteServicio.crearCliente("Cliente 1", "cliente1@test.com");
        assertEquals(1, clienteServicio.contarClientes());
        
        clienteServicio.crearCliente("Cliente 2", "cliente2@test.com");
        assertEquals(2, clienteServicio.contarClientes());
        
        Cliente cliente3 = clienteServicio.crearCliente("Cliente 3", "cliente3@test.com");
        assertEquals(3, clienteServicio.contarClientes());
        
        clienteServicio.eliminarCliente(cliente3.getId());
        assertEquals(2, clienteServicio.contarClientes());
    }
}
