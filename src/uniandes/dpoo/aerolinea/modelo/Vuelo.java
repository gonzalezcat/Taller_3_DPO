package uniandes.dpoo.aerolinea.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

/**
 * Representa un vuelo programado de la aerolínea.
 */
public class Vuelo
{
    private String fecha;
    private Ruta ruta;
    private Avion avion;
    private List<Tiquete> tiquetes;
    private boolean realizado;

    public Vuelo(String fecha, Ruta ruta, Avion avion)
    {
        this.fecha = fecha;
        this.ruta = ruta;
        this.avion = avion;
        this.tiquetes = new ArrayList<>();
        this.realizado = false;
    }

    // ---------------- Getters ----------------

    public String getFecha()
    {
        return fecha;
    }

    public Ruta getRuta()
    {
        return ruta;
    }

    public Avion getAvion()
    {
        return avion;
    }

    public boolean isRealizado()
    {
        return realizado;
    }

    public void setRealizado(boolean realizado)
    {
        this.realizado = realizado;
    }

    public Collection<Tiquete> getTiquetes()
    {
        return tiquetes;
    }

    // ---------------- Funcionalidad ----------------

    /**
     * @return capacidad total del avión
     */
    public int getCapacidadTotal()
    {
        return avion.getCapacidad();
    }

    /**
     * @return capacidad disponible del avión en este vuelo
     */
    public int getCapacidadDisponible()
    {
        return getCapacidadTotal() - tiquetes.size();
    }

    /**
     * Vende un tiquete a un cliente y lo asocia al vuelo.
     * @param cliente Cliente comprador
     * @return Tiquete creado
     */
    public Tiquete venderTiqueteA(Cliente cliente)
    {
        int tarifa = ruta.calcularTarifa(cliente, this);
        Tiquete t = GeneradorTiquetes.generarTiquete(this, cliente, tarifa);
        tiquetes.add(t);
        return t;
    }
}
