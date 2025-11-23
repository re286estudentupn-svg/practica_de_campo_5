package sistema;
import java.util.ArrayList;
import java.util.List;

public class MainPrueba {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION DE CITAS RURALES v1.0 ===");
        
        GestorArchivos gestor = new GestorArchivos();
        List<Paciente> listaPacientes = new ArrayList<>();

        // 1. SIMULACION DE REGISTRO
        System.out.println("\n[1] Registrando pacientes...");
        listaPacientes.add(new Paciente("40001111", "Renzo Palomino", "999111222", "renzo@mail.com", "Av. Peru 123", null));
        listaPacientes.add(new Paciente("10203040", "Maria Lopez", "988777666", "maria@mail.com", "Jr. Callao 456", null));
        listaPacientes.add(new Paciente("77665544", "Juan Perez", "911222333", "juan@mail.com", "Calle Real 789", null));

        // 2. GUARDADO (Persistencia)
        System.out.println("[2] Guardando en disco...");
        gestor.guardarPacientes(listaPacientes);

        // 3. LECTURA (Prueba de que funciona)
        System.out.println("\n[3] Verificando lectura de datos...");
        List<Paciente> recuperados = gestor.cargarPacientes();
        
        for (Paciente p : recuperados) {
            System.out.println("   -> " + p.toString());
        }
        
        System.out.println("\n=== PRUEBA FINALIZADA CORRECTAMENTE ===");
    }
}