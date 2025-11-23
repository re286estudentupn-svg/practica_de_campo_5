package sistema;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {
    // Nombre del archivo que se creará en la carpeta del proyecto
    private static final String NOMBRE_ARCHIVO = "pacientes_data.csv";

    // GUARDAR DATOS
    public void guardarPacientes(List<Paciente> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOMBRE_ARCHIVO))) {
            // Escribir cabecera del CSV
            writer.write("DNI,NOMBRE,TELEFONO,CORREO,DIRECCION");
            writer.newLine();
            
            for (Paciente p : lista) {
                String linea = p.getDni() + "," + p.getNombre() + "," + p.getTelefono() + "," + p.getCorreo() + "," + p.getDireccion();
                writer.write(linea);
                writer.newLine();
            }
            System.out.println("EXITO: Base de datos guardada en " + NOMBRE_ARCHIVO);
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // CARGAR DATOS
    public List<Paciente> cargarPacientes() {
        List<Paciente> lista = new ArrayList<>();
        File archivo = new File(NOMBRE_ARCHIVO);
        
        if (!archivo.exists()) return lista;

        try (BufferedReader reader = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) { primeraLinea = false; continue; } // Saltar cabecera
                
                String[] d = linea.split(",");
                if (d.length >= 5) {
                    lista.add(new Paciente(d[0], d[1], d[2], d[3], d[4], null));
                }
            }
            System.out.println("EXITO: Se recuperaron " + lista.size() + " pacientes del sistema.");
        } catch (IOException e) {
            System.err.println("Error al leer: " + e.getMessage());
        }
        return lista;
    }
}