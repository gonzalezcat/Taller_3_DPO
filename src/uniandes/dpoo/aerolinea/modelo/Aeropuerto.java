package uniandes.dpoo.aerolinea.modelo;

import java.util.HashSet;
import java.util.Set;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;

/**
 * Esta clase encapsula la información sobre los aeropuertos e implementa algunas operaciones relacionadas con la ubicación geográfica de los aeropuertos.
 * 
 * No puede haber dos aeropuertos con el mismo código.
 */
public class Aeropuerto
{
    // TODO completar
	private static final double RADIO_TERRESTRE = 6371.0;
	private static Set<String> codigosRegistrados = new HashSet<String>( );

    /**
     * unicidad
     */
    private String codigo;

    /**
     * Nombre del aeropuerto
     */
    private String nombre;

    /**
     * Nombre de la ciudad donde está el aeropuerto
     */
    private String nombreCiudad;

    /**
     * Latitud (-90 .. 90)
     */
    private double latitud;

    /**
     * Longitud (-180 .. 180)
     */
    private double longitud;

    /**
     * Construye un nuevo aeropuerto e inicializa sus atributos con los valores dados.
     * El código del nuevo aeropuerto queda registrado en el conjunto de códigos
     * y, si ya existía, lanza AeropuertoDuplicadoException.
     *
     * @param nombre Nombre del aeropuerto
     * @param codigo Código del aeropuerto (único)
     * @param nombreCiudad Nombre de la ciudad
     * @param latitud Latitud (-90 .. 90)
     * @param longitud Longitud (-180 .. 180)
     * @throws AeropuertoDuplicadoException Si ya existe un aeropuerto con el mismo código
     */
    public Aeropuerto( String nombre, String codigo, String nombreCiudad, double latitud, double longitud ) throws AeropuertoDuplicadoException
    {
        if( codigo == null ) codigo = "";
        if( codigosRegistrados.contains( codigo ) )
            throw new AeropuertoDuplicadoException( codigo );

        this.codigo = codigo;
        this.nombre = nombre;
        this.nombreCiudad = nombreCiudad;
        this.latitud = latitud;
        this.longitud = longitud;

        codigosRegistrados.add( codigo );
    }

    /* Getters */
    public String getCodigo( )
    {
        return codigo;
    }

    public String getNombre( )
    {
        return nombre;
    }

    public String getNombreCiudad( )
    {
        return nombreCiudad;
    }

    public double getLatitud( )
    {
        return latitud;
    }

    public double getLongitud( )
    {
        return longitud;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( o == null || !( o instanceof Aeropuerto ) ) return false;
        Aeropuerto a = ( Aeropuerto ) o;
        return this.codigo != null && this.codigo.equals( a.getCodigo( ) );
    }

    @Override
    public int hashCode( )
    {
        return ( codigo == null ) ? 0 : codigo.hashCode( );
    }

    @Override
    public String toString( )
    {
        return String.format( "Aeropuerto[%s - %s, %s (lat: %.6f, lon: %.6f)]", codigo, nombre, nombreCiudad, latitud, longitud );
    }

    /**
     * Calcula la distancia aproximada entre dos aeropuertos km
     * Método estático utilitario
     *
     * @param aeropuerto1
     * @param aeropuerto2
     * @return distancia aproximada en kilómetros (int, redondeada)
     */
    public static int calcularDistancia( Aeropuerto aeropuerto1, Aeropuerto aeropuerto2 )
    {
        //rad
        double latAeropuerto1 = Math.toRadians( aeropuerto1.getLatitud( ) );
        double lonAeropuerto1 = Math.toRadians( aeropuerto1.getLongitud( ) );
        double latAeropuerto2 = Math.toRadians( aeropuerto2.getLatitud( ) );
        double lonAeropuerto2 = Math.toRadians( aeropuerto2.getLongitud( ) );

        // dist lat lon
        double deltaX = ( lonAeropuerto2 - lonAeropuerto1 ) * Math.cos( ( latAeropuerto1 + latAeropuerto2 ) / 2 );
        double deltaY = ( latAeropuerto2 - latAeropuerto1 );

        // dist real
        double distancia = Math.sqrt( deltaX * deltaX + deltaY * deltaY ) * RADIO_TERRESTRE;

        return ( int )Math.round( distancia );
    }

}
