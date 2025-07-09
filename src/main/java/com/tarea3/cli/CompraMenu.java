package com.tarea3.cli;

import com.tarea3.modelo.Cliente;
import com.tarea3.modelo.Compra;
import com.tarea3.servicio.ClienteServicio;
import com.tarea3.servicio.CompraServicio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class CompraMenu {
    private final CompraServicio compraServicio;
    private final ClienteServicio clienteServicio;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public CompraMenu(CompraServicio compraServicio, ClienteServicio clienteServicio) {
        this.compraServicio = compraServicio;
        this.clienteServicio = clienteServicio;
    }
    
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            ConsoleUtils.limpiarPantalla();
            System.out.println("=".repeat(50));
            ConsoleUtils.mostrarTitulo("GESTIÓN DE COMPRAS");
            System.out.println("=".repeat(50));
            System.out.println("1. Registrar compra");
            System.out.println("2. Listar compras");
            System.out.println("3. Buscar compra");
            System.out.println("4. Compras por cliente");
            System.out.println("5. Eliminar compra");
            System.out.println("6. Mostrar estadísticas");
            System.out.println("0. Volver al menú principal");
            System.out.println("=".repeat(50));
            
            int opcion = ConsoleUtils.leerOpcionMenu("Seleccione una opción: ", 0, 6);
            
            switch (opcion) {
                case 1 -> registrarCompra();
                case 2 -> listarCompras();
                case 3 -> buscarCompra();
                case 4 -> comprasPorCliente();
                case 5 -> eliminarCompra();
                case 6 -> mostrarEstadisticas();
                case 0 -> continuar = false;
            }
        }
    }
    
    private void registrarCompra() {
        System.out.println("\n--- Registrar Compra ---");
        
        try {
            int idCliente = ConsoleUtils.leerEnteroPositivo("ID del cliente: ");
            
            Optional<Cliente> clienteOpt = clienteServicio.buscarPorId(idCliente);
            if (clienteOpt.isEmpty()) {
                ConsoleUtils.mostrarError("Error: No existe un cliente con ID " + idCliente);
                ConsoleUtils.pausar();
                return;
            }
            
            Cliente cliente = clienteOpt.get();
            int monto = ConsoleUtils.leerEnteroNoNegativo("Monto de la compra: $");
            LocalDate fecha = leerFecha("Fecha de la compra (dd/MM/yyyy)");
            
            System.out.println("\nDetalles de la compra:");
            System.out.println("Cliente: " + cliente.getNombre() + " (" + cliente.getCorreo() + ")");
            System.out.println("Monto: $" + monto);
            System.out.println("Fecha: " + fecha.format(formatter));
            
            if (ConsoleUtils.confirmar("¿Confirma el registro de la compra?")) {
                int puntosGanados = compraServicio.procesarCompra(cliente, monto, fecha);
                clienteServicio.agregarPuntos(cliente.getId(), puntosGanados);
                
                Cliente clienteActualizado = clienteServicio.buscarPorId(cliente.getId()).get();
                
                ConsoleUtils.mostrarExito("Compra registrada exitosamente.");
                System.out.println("Puntos ganados: " + puntosGanados);
                System.out.println("Puntos totales: " + clienteActualizado.getPuntos());
                System.out.println("Nivel actual: " + clienteActualizado.getNivel());
                
                if (clienteActualizado.getNivel() != cliente.getNivel()) {
                    ConsoleUtils.mostrarExito(cliente.getNombre() + " ha subido de nivel: " + 
                                     cliente.getNivel() + " -> " + clienteActualizado.getNivel());
                }
            } else {
                System.out.println("Registro cancelado.");
            }
            
        } catch (IllegalArgumentException e) {
            ConsoleUtils.mostrarError("Error: " + e.getMessage());
        }
        
        ConsoleUtils.pausar();
    }
    
    private void listarCompras() {
        System.out.println("\n--- Lista de Compras ---");
        
        List<Compra> compras = compraServicio.obtenerTodas();
        
        if (compras.isEmpty()) {
            System.out.println("No hay compras registradas.");
        } else {
            System.out.println("Total de compras: " + compras.size());
            System.out.println("-".repeat(70));
            System.out.printf("%-8s %-10s %-10s %-12s %-15s%n", 
                            "ID", "Cliente", "Monto", "Fecha", "Puntos Base");
            System.out.println("-".repeat(70));
            
            for (Compra compra : compras) {
                System.out.printf("%-8d %-10d $%-9d %-12s %-15d%n",
                                compra.getIdCompra(),
                                compra.getIdCliente(),
                                compra.getMonto(),
                                compra.getFecha().format(formatter),
                                compra.calcularPuntosBase());
            }
        }
        
        ConsoleUtils.pausar();
    }
    
    private void buscarCompra() {
        System.out.println("\n--- Buscar Compra ---");
        
        int id = ConsoleUtils.leerEnteroPositivo("ID de la compra: ");
        Optional<Compra> compraOpt = compraServicio.buscarPorId(id);
        
        if (compraOpt.isPresent()) {
            ConsoleUtils.mostrarExito("Compra encontrada:");
            mostrarDetalleCompra(compraOpt.get());
        } else {
            ConsoleUtils.mostrarError("Compra no encontrada.");
        }
        
        ConsoleUtils.pausar();
    }
    
    private void comprasPorCliente() {
        System.out.println("\n--- Compras por Cliente ---");
        
        int idCliente = ConsoleUtils.leerEnteroPositivo("ID del cliente: ");
        Optional<Cliente> clienteOpt = clienteServicio.buscarPorId(idCliente);
        
        if (clienteOpt.isEmpty()) {
            ConsoleUtils.mostrarError("Cliente no encontrado.");
            ConsoleUtils.pausar();
            return;
        }
        
        Cliente cliente = clienteOpt.get();
        List<Compra> compras = compraServicio.obtenerPorCliente(idCliente);
        
        System.out.println("Cliente: " + cliente.getNombre() + " (" + cliente.getCorreo() + ")");
        System.out.println("Nivel actual: " + cliente.getNivel() + " (" + cliente.getPuntos() + " puntos)");
        
        if (compras.isEmpty()) {
            System.out.println("Este cliente no tiene compras registradas.");
        } else {
            System.out.println("Total de compras: " + compras.size());
            System.out.println("-".repeat(60));
            System.out.printf("%-8s %-10s %-12s %-15s%n", 
                            "ID", "Monto", "Fecha", "Puntos Base");
            System.out.println("-".repeat(60));
            
            int totalMonto = 0;
            int totalPuntosBase = 0;
            
            for (Compra compra : compras) {
                totalMonto += compra.getMonto();
                totalPuntosBase += compra.calcularPuntosBase();
                
                System.out.printf("%-8d $%-9d %-12s %-15d%n",
                                compra.getIdCompra(),
                                compra.getMonto(),
                                compra.getFecha().format(formatter),
                                compra.calcularPuntosBase());
            }
            
            System.out.println("-".repeat(60));
            System.out.println("Total gastado: $" + totalMonto);
            System.out.println("Total puntos base: " + totalPuntosBase);
        }
        
        ConsoleUtils.pausar();
    }
    
    private void eliminarCompra() {
        System.out.println("\n--- Eliminar Compra ---");
        
        int id = ConsoleUtils.leerEnteroPositivo("ID de la compra a eliminar: ");
        Optional<Compra> compraOpt = compraServicio.buscarPorId(id);
        
        if (compraOpt.isEmpty()) {
            ConsoleUtils.mostrarError("Compra no encontrada.");
            ConsoleUtils.pausar();
            return;
        }
        
        Compra compra = compraOpt.get();
        System.out.println("Compra a eliminar:");
        mostrarDetalleCompra(compra);
        
        if (ConsoleUtils.confirmar("¿Está seguro que desea eliminar esta compra?")) {
            boolean eliminado = compraServicio.eliminarCompra(id);
            if (eliminado) {
                ConsoleUtils.mostrarExito("Compra eliminada exitosamente.");
                ConsoleUtils.mostrarAdvertencia("Nota: Los puntos del cliente no se actualizan automáticamente.");
            } else {
                ConsoleUtils.mostrarError("Error al eliminar la compra.");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
        
        ConsoleUtils.pausar();
    }
    
    private void mostrarEstadisticas() {
        System.out.println("\n--- Estadísticas de Compras ---");
        
        List<Compra> compras = compraServicio.obtenerTodas();
        
        if (compras.isEmpty()) {
            System.out.println("No hay compras registradas.");
        } else {
            int totalCompras = compras.size();
            int montoTotal = 0;
            int puntosBaseTotal = 0;
            
            for (Compra compra : compras) {
                montoTotal += compra.getMonto();
                puntosBaseTotal += compra.calcularPuntosBase();
            }
            
            double promedioMonto = (double) montoTotal / totalCompras;
            double promedioPuntos = (double) puntosBaseTotal / totalCompras;
            
            System.out.println("Total de compras: " + totalCompras);
            System.out.println("Monto total: $" + montoTotal);
            System.out.println("Promedio por compra: $" + String.format("%.2f", promedioMonto));
            System.out.println("Puntos base totales: " + puntosBaseTotal);
            System.out.println("Promedio puntos por compra: " + String.format("%.2f", promedioPuntos));
        }
        
        ConsoleUtils.pausar();
    }
    
    private LocalDate leerFecha(String prompt) {
        while (true) {
            String input = ConsoleUtils.leerStringOpcional(prompt + " (Enter para hoy): ");
            
            if (input.isEmpty()) {
                return LocalDate.now();
            }
            
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                ConsoleUtils.mostrarAdvertencia("Formato de fecha inválido. Use dd/MM/yyyy (ejemplo: 15/06/2024)");
            }
        }
    }
    
    private void mostrarDetalleCompra(Compra compra) {
        System.out.println("-".repeat(40));
        System.out.println("ID Compra: " + compra.getIdCompra());
        System.out.println("ID Cliente: " + compra.getIdCliente());
        System.out.println("Monto: $" + compra.getMonto());
        System.out.println("Fecha: " + compra.getFecha().format(formatter));
        System.out.println("Puntos base: " + compra.calcularPuntosBase());
        System.out.println("-".repeat(40));
    }
}
