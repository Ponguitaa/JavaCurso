package org.alex;

import org.alex.dao.*;
import org.alex.model.Categoria;
import org.alex.model.Empleado;
import org.alex.model.Producto;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {

    private KeyboardReader reader;
    private EmpleadoDao dao;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;

    public Menu() {
        reader = new KeyboardReader();
        dao = EmpleadoDaoImpl.getInstance();
        categoriaDao = CategoriaDaoImpl.getInstance();
        productoDao = ProductoDaoImpl.getInstance();
    }

    public void init() {

        int opcion;

        do {
            menu();
            opcion = reader.nextInt();

            switch (opcion) {
                case 1: listAll();
                    break;
                case 2: listById();
                    break;
                case 3: insert();
                    break;
                case 4: update();
                    break;
                case 5: delete();
                    break;
                case 6: listAllProductos();
                    break;
                case 7: insertProducto();
                    break;
                case 8: listAllCategorias();
                    break;
                case 9: insertCategoria();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.err.println("\nEl número introducido no se corresponde con una operación válida\n\n");
            }
        } while (opcion != 0);
    }

    public void menu() {

        System.out.println("SISTEMA DE GESTIÓN DE EMPLEADOS");
        System.out.println("===============================\n");
        System.out.println("\n-> Introduzca una opción de entre las siguientes\n");
        System.out.println("0: Salir");
        System.out.println("1: Listar todos los empleados");
        System.out.println("2: Listar un empleado por su ID");
        System.out.println("3: Insertar un nuevo empleado");
        System.out.println("4: Actualizar un empleado");
        System.out.println("5: Eliminar un empleado");
        System.out.println("6: Listar todos los productos");
        System.out.println("7: Insertar un producto");
        System.out.println("8: Listar todas las categorías");
        System.out.println("9: Insertar una categoría");
        System.out.print("\nOpción: ");
    }

    public void insert() {

        System.out.println("\nINSERCIÓN DE UN NUEVO EMPLEADO");
        System.out.println("------------------------------\n");
        System.out.print("Introduzca el nombre (sin apellidos) del empleado: ");
        String nombre = reader.nextLine();
        System.out.print("Introduzca los apellidos del empleado: ");
        String apellidos = reader.nextLine();
        System.out.print("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa): ");
        LocalDate fechaNacimiento = reader.nextLocalDate();
        System.out.print("Introduzca el puesto del empleado: ");
        String puesto = reader.nextLine();
        System.out.print("Introduzca el email del nuevo empleado: ");
        String email = reader.nextLine();


        try {
            dao.add(new Empleado(nombre, apellidos, fechaNacimiento, puesto, email));
            System.out.println("Nuevo empleado registrado");
        } catch (SQLException ex) {
            System.err.println("Error insertando el nuevo registro en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");


    }


    public void listAll() {
        System.out.println("\nLISTADO DE TODOS LOS EMPLEADOS");
        System.out.println("------------------------------\n");


        try {

            List<Empleado> result = dao.getAll();

            if (result.isEmpty())
                System.out.println("No hay empleados registrados en la base de datos");
            else {
                printCabeceraTablaEmpleado();
                result.forEach(this::printEmpleado);
                System.out.println("\n");

            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");


    }

    public void listById() {
        System.out.println("\nBÚSQUEDA DE EMPLEADOS POR ID");
        System.out.println("------------------------------\n");

        try {


            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado =  dao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
                System.out.println("\n");

            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");


    }


    public void update() {

        System.out.println("\nACTUALIZACIÓN DE UN EMPLEADO");
        System.out.println("------------------------------\n");

        try {


            System.out.print("Introduzca el ID del empleado a buscar: ");
            int id = reader.nextInt();

            Empleado empleado =  dao.getById(id);

            if (empleado == null)
                System.out.println("No hay empleados registrados en la base de datos con ese ID");
            else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
                System.out.println("\n");

                System.out.printf("Introduzca el nombre (sin apellidos) del empleado (%s): ", empleado.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? empleado.getNombre() : nombre;

                System.out.printf("Introduzca los apellidos del empleado (%s): ", empleado.getApellidos());
                String apellidos = reader.nextLine();
                apellidos = (apellidos.isBlank()) ? empleado.getApellidos() : apellidos;

                System.out.printf("Introduzca la fecha de nacimiento del empleado (formato dd/MM/aaaa) (%s): ",
                        empleado.getFecha_nacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaNacimiento = reader.nextLine();
                LocalDate fechaNacimiento = (strFechaNacimiento.isBlank()) ? empleado.getFecha_nacimiento()
                        : LocalDate.parse(strFechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("Introduzca el puesto del empleado (%s): ", empleado.getPuesto());
                String puesto = reader.nextLine();
                puesto = (puesto.isBlank()) ? empleado.getPuesto() : puesto;


                System.out.printf("Introduzca el email del nuevo empleado (%s): ", empleado.getEmail());
                String email = reader.nextLine();
                email = (email.isBlank()) ? empleado.getEmail() : email;

                empleado.setNombre(nombre);
                empleado.setApellidos(apellidos);
                empleado.setFecha_nacimiento(fechaNacimiento);
                empleado.setPuesto(puesto);
                empleado.setEmail(email);

                dao.update(empleado);

                System.out.println("");
                System.out.printf("Empleado con ID %s actualizado", empleado.getId_empleado());
                System.out.println("");

            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");



    }


    public void delete() {
        System.out.println("\nBORRADO DE UN EMPLEADO");
        System.out.println("--------------------------\n");

        try {


            System.out.print("Introduzca el ID del empleado a borrar: ");
            int id = reader.nextInt();

            System.out.printf("¿Está usted seguro de querer eliminar el empleado con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                dao.delete(id);
                System.out.printf("El empleado con ID %s se ha borrado\n", id);
            }

        } catch (SQLException ex) {
            System.err.println("Error consultando en la base de datos. Vuelva a intentarlo de nuevo o póngase en contacto con el administrador.");
        }

        System.out.println("");

    }


    private void printCabeceraTablaEmpleado() {
        System.out.printf("%2s %30s %8s %10s %25s", "ID", "NOMBRE", "FEC. NAC.", "PUESTO", "EMAIL");
        System.out.println("");
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
        System.out.println("\n");

    }

    private void printEmpleado(Empleado emp) {
        System.out.printf("%2s %30s %9s %10s %25s\n",
                emp.getId_empleado(),
                emp.getNombre() + " " + emp.getApellidos(),
                emp.getFecha_nacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
                emp.getPuesto(),
                emp.getEmail());
    }





    static class KeyboardReader {

        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(
                    new InputStreamReader(System.in)
            );
        }

        String next() {

            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException ex) {
                    System.err.println("Error leyendo del teclado");
                    ex.printStackTrace();
                }
            }
            return st.nextToken();

        }

        int nextInt() { return Integer.parseInt(next()); }

        double nextDouble() { return Double.parseDouble(next()); }

        LocalDate nextLocalDate() {
            return LocalDate.parse(next(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String nextLine() {

            String str = "";
            try {
                if (st.hasMoreElements())
                    str = st.nextToken("\n");
                else
                    str = br.readLine();
            } catch (IOException ex) {
                System.err.println("Error leyendo del teclado");
                ex.printStackTrace();
            }
            return str;
        }

    }

    public void listAllCategorias() {
        System.out.println("\nLISTADO DE TODAS LAS CATEGORÍAS");
        System.out.println("-------------------------------\n");

        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías registradas.");
            } else {
                categorias.forEach(System.out::println);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando las categorías: " + ex.getMessage());
        }
        System.out.println("");
    }

    public void insertCategoria() {
        System.out.println("\nINSERCIÓN DE NUEVA CATEGORÍA");
        System.out.println("-----------------------------\n");

        System.out.print("Introduzca el nombre de la categoría: ");
        String nombre = reader.nextLine();

        try {
            categoriaDao.add(new Categoria(nombre));
            System.out.println("Nueva categoría registrada");
        } catch (SQLException ex) {
            System.err.println("Error insertando la categoría: " + ex.getMessage());
        }
        System.out.println("");
    }

    public void listAllProductos() {
        System.out.println("\nLISTADO DE TODOS LOS PRODUCTOS");
        System.out.println("-------------------------------\n");

        try {
            List<Producto> productos = productoDao.getAll();
            if (productos.isEmpty()) {
                System.out.println("No hay productos registrados.");
            } else {
                productos.forEach(System.out::println);
            }
        } catch (SQLException ex) {
            System.err.println("Error consultando los productos: " + ex.getMessage());
        }
        System.out.println("");
    }

    public void insertProducto() {
        System.out.println("\nINSERCIÓN DE NUEVO PRODUCTO");
        System.out.println("-----------------------------\n");

        System.out.print("Introduzca el nombre del producto: ");
        String nombre = reader.nextLine();

        System.out.print("Introduzca el precio: ");
        double precio = reader.nextDouble();

        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías disponibles. Cree una primero.");
                return;
            }

            System.out.println("Seleccione una categoría por ID:");
            categorias.forEach(c -> System.out.println(c.getId() + ": " + c.getNombre()));
            int idCat = reader.nextInt();

            Categoria categoria = categoriaDao.getById(idCat);
            if (categoria == null) {
                System.out.println("Categoría no encontrada.");
                return;
            }

            productoDao.add(new Producto(nombre, precio, categoria));
            System.out.println("Nuevo producto registrado.");

        } catch (SQLException ex) {
            System.err.println("Error insertando el producto: " + ex.getMessage());
        }
        System.out.println("");
    }
}