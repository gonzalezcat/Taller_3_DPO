package uniandes.dpoo.aerolinea.modelo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaAerolinea;
import uniandes.dpoo.aerolinea.persistencia.IPersistenciaTiquetes;
import uniandes.dpoo.aerolinea.persistencia.TipoInvalidoException;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

/**
 * En esta clase se organizan todos los aspectos relacionados con una Aerolínea.
 */
public class Aerolinea
{
    private List<Avion> aviones;
    private Map<String, Ruta> rutas;
    private List<Vuelo> vuelos;
    private Map<String, Cliente> clientes;

    public Aerolinea( )
    {
        aviones = new LinkedList<>();
        rutas = new HashMap<>();
        vuelos = new LinkedList<>();
        clientes = new HashMap<>();
    }

    // -------------------- Métodos básicos --------------------

    public void agregarRuta( Ruta ruta )
    {
        this.rutas.put( ruta.getCodigoRuta(), ruta );
    }

    public void agregarAvion( Avion avion )
    {
        this.aviones.add( avion );
    }

    public void agregarCliente( Cliente cliente )
    {
        this.clientes.put( cliente.getIdentificador(), cliente );
    }

    public boolean existeCliente( String identificadorCliente )
    {
        return this.clientes.containsKey( identificadorCliente );
    }

    public Cliente getCliente( String identificadorCliente )
    {
        return this.clientes.get( identificadorCliente );
    }

    public Collection<Avion> getAviones( )
    {
        return aviones;
    }

    public Collection<Ruta> getRutas( )
    {
        return rutas.values();
    }

    public Ruta getRuta( String codigoRuta )
    {
        return rutas.get( codigoRuta );
    }

    public Collection<Vuelo> getVuelos( )
    {
        return vuelos;
    }

    public Vuelo getVuelo( String codigoRuta, String fechaVuelo )
    {
        if( codigoRuta == null || fechaVuelo == null ) return null;
        for( Vuelo v : vuelos )
        {
            if( v != null && v.getRuta() != null &&
                codigoRuta.equals( v.getRuta().getCodigoRuta() ) &&
                fechaVuelo.equals( v.getFecha() ) )
            {
                return v;
            }
        }
        return null;
    }

    public Collection<Cliente> getClientes( )
    {
        return clientes.values();
    }

    /**
     * Retorna todos los tiquetes de la aerolínea, recolectados vuelo por vuelo.
     */
    public Collection<Tiquete> getTiquetes( )
    {
        List<Tiquete> resultado = new ArrayList<>();
        for( Vuelo v : vuelos )
        {
            if( v != null )
            {
                resultado.addAll( v.getTiquetes() );
            }
        }
        return resultado;
    }

    // -------------------- Persistencia --------------------

    public void cargarAerolinea(String archivo, String tipoArchivo)
            throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
        IPersistenciaAerolinea persist = CentralPersistencia.getPersistenciaAerolinea(tipoArchivo);
        persist.cargarAerolinea(archivo, this);  // pasa la instancia actual
    }


    public void salvarAerolinea( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException
    {
        IPersistenciaAerolinea persist = CentralPersistencia.getPersistenciaAerolinea( tipoArchivo );
        persist.salvarAerolinea( archivo, this );
    }

    public void cargarTiquetes( String archivo, String tipoArchivo )
            throws TipoInvalidoException, IOException, InformacionInconsistenteException
    {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes( tipoArchivo );
        cargador.cargarTiquetes( archivo, this );
    }

    public void salvarTiquetes( String archivo, String tipoArchivo ) throws TipoInvalidoException, IOException
    {
        IPersistenciaTiquetes cargador = CentralPersistencia.getPersistenciaTiquetes( tipoArchivo );
        cargador.salvarTiquetes( archivo, this );
    }

    // -------------------- Funcionalidades --------------------

    public void programarVuelo( String fecha, String codigoRuta, String nombreAvion ) throws Exception
    {
        if( fecha == null || codigoRuta == null || nombreAvion == null )
            throw new Exception( "Parámetros inválidos para programar vuelo" );

        Ruta ruta = rutas.get( codigoRuta );
        if( ruta == null ) throw new Exception( "No existe la ruta: " + codigoRuta );

        Avion avionSeleccionado = null;
        for( Avion a : aviones )
        {
            if( a != null && nombreAvion.equals( a.getNombre() ) )
            {
                avionSeleccionado = a;
                break;
            }
        }
        if( avionSeleccionado == null ) throw new Exception( "No existe el avión: " + nombreAvion );

        for( Vuelo v : vuelos )
        {
            if( v != null && v.getAvion() != null &&
                v.getAvion().equals( avionSeleccionado ) &&
                v.getFecha().equals( fecha ) )
            {
                throw new Exception( "El avión ya está asignado a otro vuelo en la misma fecha: " + fecha );
            }
        }

        Vuelo nuevo = new Vuelo(fecha, ruta, avionSeleccionado );
        vuelos.add( nuevo );
    }

    public int venderTiquetes( String identificadorCliente, String fecha, String codigoRuta, int cantidad )
            throws VueloSobrevendidoException, Exception
    {
        Cliente cliente = clientes.get( identificadorCliente );
        if( cliente == null )
            throw new Exception( "No existe cliente con id: " + identificadorCliente );

        Vuelo vuelo = getVuelo( codigoRuta, fecha );
        if( vuelo == null )
            throw new Exception( "No existe vuelo con ruta " + codigoRuta + " en fecha " + fecha );

        if( vuelo.getCapacidadDisponible() < cantidad )
            throw new VueloSobrevendidoException( vuelo );

        int total = 0;
        for( int i = 0; i < cantidad; i++ )
        {
            Tiquete t = vuelo.venderTiqueteA( cliente );
            cliente.agregarTiquete( t );
            total += t.getTarifa();
        }
        return total;
    }

    public void registrarVueloRealizado( String fecha, String codigoRuta )
    {
        Vuelo vuelo = getVuelo( codigoRuta, fecha );
        if( vuelo != null )
        {
            vuelo.setRealizado( true );
        }
    }

    public String consultarSaldoPendienteCliente( String identificadorCliente )
    {
        if( identificadorCliente == null ) return "0.0";
        Cliente c = clientes.get( identificadorCliente );
        if( c == null ) return "0.0";

        double suma = 0.0;
        Collection<Tiquete> tiqs = c.getTiquetesSinUsar();
        if( tiqs != null )
        {
            for( Tiquete t : tiqs )
            {
                if( t != null ) suma += t.getTarifa();
            }
        }
        return Double.toString( suma );
    }
}
