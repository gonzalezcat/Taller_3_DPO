package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

/**
 * Clase abstracta que representa a un cliente de la aerolínea.
 */
public abstract class Cliente
{
    private String identificador;
    private String nombre;

    // Lista de tiquetes asociados al cliente
    private List<Tiquete> tiquetes;

    public Cliente()
    {
        this.identificador = "";
        this.nombre = "";
        this.tiquetes = new ArrayList<>();
    }

    public Cliente(String identificador, String nombre)
    {
        this.identificador = identificador;
        this.nombre = nombre;
        this.tiquetes = new ArrayList<>();
    }

    // ------------------ Métodos abstractos ------------------
    public abstract String getTipoCliente();

    // ------------------ Getters & Setters -------------------
    public String getIdentificador()
    {
        return identificador;
    }

    public void setIdentificador(String identificador)
    {
        this.identificador = identificador;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    // ------------------ Manejo de tiquetes ------------------
    public void agregarTiquete(Tiquete t)
    {
        if (t != null)
            this.tiquetes.add(t);
    }

    public Collection<Tiquete> getTiquetes()
    {
        return tiquetes;
    }

    public Collection<Tiquete> getTiquetesSinUsar()
    {
        List<Tiquete> libres = new ArrayList<>();
        for (Tiquete t : tiquetes)
        {
            if (t != null && !t.esUsado())
                libres.add(t);
        }
        return libres;
    }
}
