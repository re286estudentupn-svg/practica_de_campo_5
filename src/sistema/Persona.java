package sistema;

public class Persona {
    protected String dni;
    protected String nombre;
    protected String telefono;
    protected String correo;

    public Persona(String dni, String nombre, String telefono, String correo) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
}