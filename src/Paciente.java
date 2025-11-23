import java.util.Date;

/**
 * Paciente hereda de Persona.
 * Se asume que Persona define al menos: dni, nombre, telefono, correo,
 * con sus getters correspondientes (getDni, getNombre, getTelefono, getCorreo).
 */
public class Paciente extends Persona {

    private String direccion;
    private Date fechaNacimiento; // opcional en esta práctica

    public Paciente(String dni, String nombre, String telefono, String correo,
                    String direccion, Date fechaNacimiento) {
        super(dni, nombre, telefono, correo);
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() { return direccion; }
    public Date getFechaNacimiento() { return fechaNacimiento; }

    /** Unicidad por DNI: dos Paciente son iguales si comparten el mismo DNI. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente p = (Paciente) o;
        return getDni() != null && getDni().equals(p.getDni());
    }

    @Override
    public int hashCode() {
        return getDni() == null ? 0 : getDni().hashCode();
    }

    @Override
    public String toString() {
        return "Paciente{dni=" + getDni() +
                ", nombre=" + getNombre() +
                ", telefono=" + getTelefono() +
                ", correo=" + getCorreo() +
                ", direccion=" + direccion + "}";
    }
}
