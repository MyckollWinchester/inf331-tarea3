package com.tarea3.cli;

import com.tarea3.modelo.Cliente;
import com.tarea3.servicio.ClienteServicio;
import java.util.List;
import java.util.Optional;

public class ClienteMenu {
    private final ClienteServicio clienteServicio;
    
    public ClienteMenu(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }
    
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            ConsoleUtils.limpiarPantalla();
            System.out.println("=".repeat(50));
            ConsoleUtils.mostrarTitulo("GESTIÓN DE CLIENTES");
            System.out.println("=".repeat(50));
            System.out.println("1. Agregar cliente");
            System.out.println("2. Listar clientes");
            System.out.println("3. Buscar cliente");
            System.out.println("4. Actualizar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Mostrar estadísticas");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));
            
            int opcion = ConsoleUtils.leerOpcionMenu("Seleccione una opción: ", 0, 6);
            
            switch (opcion) {
                case 1 -> agregarCliente();
                case 2 -> listarClientes();
                case 3 -> buscarCliente();
                case 4 -> actualizarCliente();
                case 5 -> eliminarCliente();
                case 6 -> mostrarEstadisticas();
                case 0 -> continuar = false;
            }
        }
    }
    
    private void agregarCliente() {
        System.out.println("\n--- Agregar Cliente ---");
        
        try {
            String nombre = ConsoleUtils.leerString("Nombre del cliente: ");
            String correo = ConsoleUtils.leerString("Correo electrónico: ");
            
            Cliente cliente = clienteServicio.crearCliente(nombre, correo);
            ConsoleUtils.mostrarExito("Cliente creado exitosamente con ID " + cliente.getId() + ":");
            mostrarDetalleCliente(cliente);
            
        } catch (IllegalArgumentException e) {
            ConsoleUtils.mostrarError(e.getMessage());
        }
        
        ConsoleUtils.pausar();
    }
    
    private void listarClientes() {
        System.out.println("\n--- Lista de Clientes ---");
        
        List<Cliente> clientes = clienteServicio.obtenerTodos();
        
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            System.out.println("Total de clientes: " + clientes.size());
            System.out.println("-".repeat(80));
            System.out.printf("%-5s %-20s %-25s %-8s %-10s%n", 
                            "ID", "Nombre", "Correo", "Puntos", "Nivel");
            System.out.println("-".repeat(80));
            
            for (Cliente cliente : clientes) {
                System.out.printf("%-5d %-20s %-25s %-8d %-10s%n",
                                cliente.getId(),
                                cliente.getNombre(),
                                cliente.getCorreo(),
                                cliente.getPuntos(),
                                cliente.getNivel());
            }
        }
        
        ConsoleUtils.pausar();
    }
    
    private void buscarCliente() {
        System.out.println("\n--- Buscar Cliente ---");
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por correo");
        
        int opcion = ConsoleUtils.leerOpcionMenu("Seleccione tipo de búsqueda: ", 1, 2);
        
        Optional<Cliente> clienteOpt = Optional.empty();
        
        if (opcion == 1) {
            int id = ConsoleUtils.leerEnteroPositivo("ID del cliente: ");
            clienteOpt = clienteServicio.buscarPorId(id);
        } else {
            String correo = ConsoleUtils.leerString("Correo del cliente: ");
            clienteOpt = clienteServicio.buscarPorCorreo(correo);
        }
        
        if (clienteOpt.isPresent()) {
            ConsoleUtils.mostrarExito("Cliente encontrado:");
            mostrarDetalleCliente(clienteOpt.get());
        } else {
            ConsoleUtils.mostrarError("Cliente no encontrado.");
        }
        
        ConsoleUtils.pausar();
    }
    
    private void actualizarCliente() {
        System.out.println("\n--- Actualizar Cliente ---");
        
        int id = ConsoleUtils.leerEnteroPositivo("ID del cliente a actualizar: ");
        Optional<Cliente> clienteOpt = clienteServicio.buscarPorId(id);
        
        if (clienteOpt.isEmpty()) {
            ConsoleUtils.mostrarError("Cliente no encontrado.");
            ConsoleUtils.pausar();
            return;
        }
        
        Cliente cliente = clienteOpt.get();
        System.out.println("Cliente actual:");
        mostrarDetalleCliente(cliente);
        
        System.out.println("\n¿Qué desea actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Correo");
        System.out.println("0. Cancelar");
        
        int opcion = ConsoleUtils.leerOpcionMenu("Seleccione una opción: ", 0, 2);
        
        try {
            switch (opcion) {
                case 1 -> {
                    String nuevoNombre = ConsoleUtils.leerString("Nuevo nombre: ");
                    cliente = clienteServicio.actualizarNombre(id, nuevoNombre);
                    ConsoleUtils.mostrarExito("Nombre actualizado exitosamente.");
                }
                case 2 -> {
                    String nuevoCorreo = ConsoleUtils.leerString("Nuevo correo: ");
                    cliente = clienteServicio.actualizarCorreo(id, nuevoCorreo);
                    ConsoleUtils.mostrarExito("Correo actualizado exitosamente.");
                }
                case 0 -> {
                    System.out.println("Actualización cancelada.");
                    ConsoleUtils.pausar();
                    return;
                }
            }
            
            System.out.println("Cliente actualizado:");
            mostrarDetalleCliente(cliente);
            
        } catch (IllegalArgumentException e) {
            ConsoleUtils.mostrarError(e.getMessage());
        }
        
        ConsoleUtils.pausar();
    }
    
    private void eliminarCliente() {
        System.out.println("\n--- Eliminar Cliente ---");
        
        int id = ConsoleUtils.leerEnteroPositivo("ID del cliente a eliminar: ");
        Optional<Cliente> clienteOpt = clienteServicio.buscarPorId(id);
        
        if (clienteOpt.isEmpty()) {
            ConsoleUtils.mostrarError("Cliente no encontrado.");
            ConsoleUtils.pausar();
            return;
        }
        
        Cliente cliente = clienteOpt.get();
        System.out.println("Cliente a eliminar:");
        mostrarDetalleCliente(cliente);
        
        if (ConsoleUtils.confirmar("¿Está seguro que desea eliminar este cliente?")) {
            boolean eliminado = clienteServicio.eliminarCliente(id);
            if (eliminado) {
                ConsoleUtils.mostrarExito("Cliente eliminado exitosamente.");
            } else {
                ConsoleUtils.mostrarError("Error al eliminar el cliente.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
        
        ConsoleUtils.pausar();
    }
    
    private void mostrarEstadisticas() {
        System.out.println("\n--- Estadísticas de Clientes ---");
        
        List<Cliente> clientes = clienteServicio.obtenerTodos();
        
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            int totalClientes = clientes.size();
            int clientesBronce = 0, clientesPlata = 0, clientesOro = 0, clientesPlatino = 0;
            int totalPuntos = 0;
            
            for (Cliente cliente : clientes) {
                totalPuntos += cliente.getPuntos();
                switch (cliente.getNivel()) {
                    case BRONCE -> clientesBronce++;
                    case PLATA -> clientesPlata++;
                    case ORO -> clientesOro++;
                    case PLATINO -> clientesPlatino++;
                }
            }
            
            double promedioPuntos = (double) totalPuntos / totalClientes;
            
            System.out.println("Total de clientes: " + totalClientes);
            System.out.println("Total de puntos: " + totalPuntos);
            System.out.println("Promedio de puntos: " + String.format("%.2f", promedioPuntos));
            System.out.println();
            System.out.println("Distribución por nivel:");
            System.out.println("  Bronce: " + clientesBronce + " (" + String.format("%.1f", 100.0 * clientesBronce / totalClientes) + "%)");
            System.out.println("  Plata: " + clientesPlata + " (" + String.format("%.1f", 100.0 * clientesPlata / totalClientes) + "%)");
            System.out.println("  Oro: " + clientesOro + " (" + String.format("%.1f", 100.0 * clientesOro / totalClientes) + "%)");
            System.out.println("  Platino: " + clientesPlatino + " (" + String.format("%.1f", 100.0 * clientesPlatino / totalClientes) + "%)");
        }
        
        ConsoleUtils.pausar();
    }
    
    private void mostrarDetalleCliente(Cliente cliente) {
        System.out.println("-".repeat(40));
        System.out.println("ID: " + cliente.getId());
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Correo: " + cliente.getCorreo());
        System.out.println("Puntos: " + cliente.getPuntos());
        System.out.println("Nivel: " + cliente.getNivel());
        System.out.println("-".repeat(40));
    }
}
