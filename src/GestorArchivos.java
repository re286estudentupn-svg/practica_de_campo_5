import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gestor de persistencia simple basado en archivos CSV (UTF-8).
 * - Directorio de datos: ./data
 * - Archivo por defecto: ./data/pacientes.csv
 *
 * Reglas CSV (compatibles con exportación de Excel "CSV UTF-8"):
 * - Si un campo contiene coma, comillas o salto de línea, se encierra entre comillas.
 * - Las comillas internas se duplican.
 *
 * Este gestor expone métodos para guardar y cargar una lista de Paciente.
 * Para otras entidades, crea métodos análogos siguiendo el mismo patrón.
 */
public class GestorArchivos {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path ARCHIVO_PACIENTES = DATA_DIR.resolve("pacientes.csv");

    /** Devuelve la ruta del archivo CSV por defecto para pacientes. */
    public Path rutaArchivoPacientes() {
        return ARCHIVO_PACIENTES;
    }

    /** Guarda la lista de pacientes en CSV UTF-8. Sobrescribe el archivo. */
    public void guardarPacientes(List<Paciente> lista) {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("No se pudo crear el directorio de datos: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = Files.newBufferedWriter(
                ARCHIVO_PACIENTES,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            // Cabecera (opcional; útil para inspección humana)
            bw.write("dni,nombre,telefono,correo,direccion");
            bw.newLine();

            for (Paciente p : lista) {
                List<String> cols = Arrays.asList(
                        p.getDni(),
                        p.getNombre(),
                        p.getTelefono(),
                        p.getCorreo(),
                        p.getDireccion()
                );
                bw.write(toCsvLine(cols));
                bw.newLine();
            }
            System.out.println("Guardado OK -> " + ARCHIVO_PACIENTES.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al guardar pacientes: " + e.getMessage());
        }
    }

    /** Carga pacientes desde el archivo CSV por defecto (si existe). */
    public List<Paciente> cargarPacientes() {
        return cargarPacientesDesdeCsv(ARCHIVO_PACIENTES);
    }

    /** Carga pacientes desde una ruta CSV específica (UTF-8). */
    public List<Paciente> cargarPacientesDesdeCsv(Path csv) {
        List<Paciente> out = new ArrayList<>();
        if (csv == null || !Files.exists(csv)) return out;

        try (BufferedReader br = Files.newBufferedReader(csv, StandardCharsets.UTF_8)) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                // Limpia posible BOM en primera línea
                if (first && !line.isEmpty() && line.charAt(0) == '\uFEFF') {
                    line = line.substring(1);
                }
                // Si la primera línea es cabecera, saltarla
                if (first && line.toLowerCase().contains("dni") && line.toLowerCase().contains("nombre")) {
                    first = false;
                    continue;
                }
                first = false;

                List<String> cols = parseCsv(line);
                if (cols.size() >= 5) {
                    String dni       = cols.get(0);
                    String nombre    = cols.get(1);
                    String telefono  = cols.get(2);
                    String correo    = cols.get(3);
                    String direccion = cols.get(4);
                    out.add(new Paciente(dni, nombre, telefono, correo, direccion, null));
                }
            }
            System.out.println("Cargados " + out.size() + " pacientes desde " + csv.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al leer pacientes: " + e.getMessage());
        }
        return out;
    }

    // ===================== Utilidades CSV =====================

    /** Construye una línea CSV escapando cada columna según reglas RFC 4180. */
    private static String toCsvLine(List<String> cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.size(); i++) {
            sb.append(escapeCsv(cols.get(i)));
            if (i < cols.size() - 1) sb.append(',');
        }
        return sb.toString();
    }

    /** Escapa comillas internas y envuelve el campo entre comillas si es necesario. */
    private static String escapeCsv(String v) {
        if (v == null) return "";
        boolean needQuotes = v.contains(",") || v.contains("\"") || v.contains("\n") || v.contains("\r");
        String escaped = v.replace("\"", "\"\"");
        return needQuotes ? "\"" + escaped + "\"" : escaped;
    }

    /**
     * Parser simple de CSV compatible con comillas y comillas duplicadas.
     * No usa split(",") para no romper campos entrecomillados.
     */
    private static List<String> parseCsv(String line) {
        ArrayList<String> out = new ArrayList<>();
        if (line == null) { out.add(""); return out; }

        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"'); i++; // "" -> "
                    } else {
                        inQuotes = false;     // fin del campo entrecomillado
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out;
    }
}
