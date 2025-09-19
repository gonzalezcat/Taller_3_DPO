package uniandes.dpoo.aerolinea.modelo;

/**
 * Esta clase tiene la información de una ruta entre dos aeropuertos que cubre una aerolínea.
 */
public class Ruta
{
    // TODO completar
	private String codigoRuta;

	
    private Aeropuerto origen;

    
    private Aeropuerto destino;

    
    private String horaSalida;


    private String horaLlegada;

    /**
     * Crea una ruta con la información mínima.
     *
     * @param origen Aeropuerto de origen
     * @param destino Aeropuerto de destino
     * @param horaSalida Hora de salida (string)
     * @param horaLlegada Hora de llegada (string)
     * @param codigoRuta Código de la ruta
     */
    public Ruta( Aeropuerto origen, Aeropuerto destino, String horaSalida, String horaLlegada, String codigoRuta )
    {
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.codigoRuta = codigoRuta;
    }

    /* Getters */
    public String getCodigoRuta( )
    {
        return codigoRuta;
    }

    public Aeropuerto getOrigen( )
    {
        return origen;
    }

    public Aeropuerto getDestino( )
    {
        return destino;
    }

    public String getHoraSalida( )
    {
        return horaSalida;
    }

    public String getHoraLlegada( )
    {
        return horaLlegada;
    }
    
    public static int getMinutos( String horaCompleta )
    {
        if( horaCompleta == null || horaCompleta.trim( ).length( ) == 0 ) return 0;
        String s = horaCompleta.trim( );
        if( s.length( ) <= 2 ) return Integer.parseInt( s ); // improbable, pero defensivo
        String minutosStr = s.substring( s.length( ) - 2 );
        return Integer.parseInt( minutosStr );
    }

    /**
     * Dada una cadena con una hora y minutos, retorna las horas.
     *
     * Por ejemplo, para la cadena '715' retorna 7.
     * @param horaCompleta Una cadena con una hora, donde los minutos siempre ocupan los dos últimos caracteres
     * @return Una cantidad de horas entre 0 y 23
     */
    public static int getHoras( String horaCompleta )
    {
        if( horaCompleta == null || horaCompleta.trim( ).length( ) == 0 ) return 0;
        String s = horaCompleta.trim( );
        return Integer.parseInt( s ) / 100;
    }

    @Override
    public String toString( )
    {
        return String.format( "Ruta[%s: %s -> %s, sale %s llega %s]", codigoRuta,
            origen == null ? "??" : origen.getCodigo( ),
            destino == null ? "??" : destino.getCodigo( ),
            horaSalida, horaLlegada );
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( o == null || !( o instanceof Ruta ) ) return false;
        Ruta r = ( Ruta ) o;
        return this.codigoRuta != null && this.codigoRuta.equals( r.getCodigoRuta( ) );
    }

    @Override
    public int hashCode( )
    {
        return ( codigoRuta == null ) ? 0 : codigoRuta.hashCode( );
    }

    
}
