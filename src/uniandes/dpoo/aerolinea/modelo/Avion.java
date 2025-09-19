package uniandes.dpoo.aerolinea.modelo;

public class Avion
{
    private String nombre;
    private int capacidad;

    public Avion(String nombre, int capacidad)
    {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre()
    {
        return nombre;
    }

    public int getCapacidad()
    {
        return capacidad;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Avion)) return false;
        Avion other = (Avion) o;
        return nombre != null && nombre.equals(other.getNombre());
    }

    @Override
    public int hashCode()
    {
        return (nombre == null) ? 0 : nombre.hashCode();
    }

    @Override
    public String toString()
    {
        return "Avion[" + nombre + ", cap=" + capacidad + "]";
    }
}
