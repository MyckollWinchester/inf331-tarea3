package com.tarea3.cli;

import com.tarea3.modelo.Cliente;
import com.tarea3.servicio.ClienteServicio;
import com.tarea3.servicio.CompraServicio;
import java.util.Optional;

public class MenuPrincipal {
    private final ClienteServicio clienteServicio;
    private final CompraServicio compraServicio;
    private final ClienteMenu clienteMenu;
    private final CompraMenu compraMenu;
    
    public MenuPrincipal(ClienteServicio clienteServicio, CompraServicio compraServicio) {
        this.clienteServicio = clienteServicio;
        this.compraServicio = compraServicio;
        this.clienteMenu = new ClienteMenu(clienteServicio);
        this.compraMenu = new CompraMenu(compraServicio, clienteServicio);
    }
    
    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenuPrincipal();
            int opcion = ConsoleUtils.leerOpcionMenu("Seleccione una opción: ", 0, 4);
            
            switch (opcion) {
                case 1 -> clienteMenu.mostrarMenu();
                case 2 -> compraMenu.mostrarMenu();
                case 3 -> mostrarPuntosNivelCliente();
                case 4 -> mostrarResumenGeneral();
                case 0 -> continuar = false;
            }
        }
    }

    private void mostrarMenuPrincipal() {
        ConsoleUtils.limpiarPantalla();
        System.out.println("=".repeat(50));
        ConsoleUtils.mostrarTitulo("MENÚ PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("1. Gestión de Clientes");
        System.out.println("2. Gestión de Compras");
        System.out.println("3. Mostrar Puntos/Nivel de Cliente");
        System.out.println("4. Resumen General");
        System.out.println("0. Salir");
        System.out.println("=".repeat(50));
    }

    private void mostrarPuntosNivelCliente() {
        System.out.println("\n--- Puntos y Nivel de Cliente ---");
        
        int id = ConsoleUtils.leerEnteroPositivo("ID del cliente: ");
        Optional<Cliente> clienteOpt = clienteServicio.buscarPorId(id);
        
        if (clienteOpt.isEmpty()) {
            ConsoleUtils.mostrarError("Cliente no encontrado.");
        } else {
            Cliente cliente = clienteOpt.get();
            mostrarInformacionCliente(cliente);
        }
        
        ConsoleUtils.pausar();
    }

    private void mostrarResumenGeneral() {
        System.out.println("\n--- Resumen General del Sistema ---");
        
        int totalClientes = clienteServicio.contarClientes();
        int totalCompras = compraServicio.obtenerTodas().size();
        
        System.out.println("Estadísticas Generales:");
        System.out.println("  Total de clientes: " + totalClientes);
        System.out.println("  Total de compras: " + totalCompras);
        
        if (totalClientes > 0) {
            System.out.println();
            System.out.println("Top 5 Clientes por Puntos:");
            clienteServicio.obtenerTodos().stream()
                .sorted((c1, c2) -> Integer.compare(c2.getPuntos(), c1.getPuntos()))
                .limit(5)
                .forEach(cliente -> {
                    System.out.printf("  %s: %d puntos (%s)%n", 
                        cliente.getNombre(), cliente.getPuntos(), cliente.getNivel());
                });
        }
        
        if (totalCompras > 0) {
            System.out.println();
            System.out.println("Estadísticas de Compras:");
            int montoTotal = compraServicio.obtenerTodas().stream()
                .mapToInt(compra -> compra.getMonto())
                .sum();
            double promedioCompra = (double) montoTotal / totalCompras;
            
            System.out.printf("  Monto total: $%,d%n", montoTotal);
            System.out.printf("  Promedio por compra: $%,.2f%n", promedioCompra);
        }
        
        ConsoleUtils.pausar();
    }
    
    private void mostrarInformacionCliente(Cliente cliente) {
        System.out.println("\n" + "=".repeat(50));
        ConsoleUtils.mostrarTitulo("INFORMACIÓN DEL CLIENTE");
        System.out.println("=".repeat(50));
        System.out.println("Nombre: " + cliente.getNombre());
        System.out.println("Correo: " + cliente.getCorreo());
        System.out.println("ID: " + cliente.getId());
        System.out.println();
        System.out.println("PUNTOS Y NIVEL:");
        System.out.println("  Puntos actuales: " + cliente.getPuntos());
        System.out.println("  Nivel actual: " + cliente.getNivel());
        System.out.println("  Multiplicador: " + cliente.getNivel().getMultiplicador() + "x");
        System.out.println();
        
        String progreso = obtenerProgresoNivel(cliente);
        System.out.println("Progreso: " + progreso);
        
        var compras = compraServicio.obtenerPorCliente(cliente.getId());
        System.out.println();
        System.out.println("HISTORIAL DE COMPRAS:");
        System.out.println("  Total de compras: " + compras.size());
        
        if (!compras.isEmpty()) {
            int montoTotal = compras.stream().mapToInt(c -> c.getMonto()).sum();
            int puntosBase = compras.stream().mapToInt(c -> c.calcularPuntosBase()).sum();
            
            System.out.println("  Monto total gastado: $" + montoTotal);
            System.out.println("  Puntos base ganados: " + puntosBase);
            System.out.println("  Última compra: " + compras.get(compras.size() - 1).getFecha());
        }
        
        System.out.println("=".repeat(50));
    }

    private String obtenerProgresoNivel(Cliente cliente) {
        int puntos = cliente.getPuntos();
        
        return switch (cliente.getNivel()) {
            case BRONCE -> {
                int falta = 500 - puntos;
                int progreso = (int) ((double) puntos / 500 * 100);
                yield progreso + "% hacia Plata (faltan " + falta + " puntos)";
            }
            case PLATA -> {
                int falta = 1500 - puntos;
                int progreso = (int) ((double) (puntos - 500) / 1000 * 100);
                yield progreso + "% hacia Oro (faltan " + falta + " puntos)";
            }
            case ORO -> {
                int falta = 3000 - puntos;
                int progreso = (int) ((double) (puntos - 1500) / 1500 * 100);
                yield progreso + "% hacia Platino (faltan " + falta + " puntos)";
            }
            case PLATINO -> "Máximo nivel alcanzado.";
        };
    }
}
