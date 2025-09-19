package uniandes.dpoo.aerolinea.modelo.cliente;

import org.json.JSONObject;

/**
 * Esta clase se usa para representar a los clientes de la aerolínea que son empresas
 */
public class ClienteCorporativo extends Cliente
{
	public static final String CORPORATIVO = "CORPORATIVO";

	/**
	 * Nombre de la empresa
	 */
	private String nombreEmpresa;

	/**
	 * Tamaño de la empresa (por ejemplo, "pequeña", "mediana", "grande" o un número)
	 */
	private String tamanoEmpresa;

	/**
	 * Constructor por defecto (delegar al padre)
	 */
	public ClienteCorporativo( )
	{
		super( );
		this.nombreEmpresa = "";
		this.tamanoEmpresa = "";
	}

	/**
	 * Constructor con datos básicos: delega al padre para identificador/nombre si aplica.
	 * Observación: la firma exacta aquí puede necesitar adaptarse si la clase Cliente define otro constructor.
	 *
	 * @param identificador identificador del cliente (rut, nit, etc.)
	 * @param nombre nombre de contacto responsable
	 * @param nombreEmpresa nombre de la empresa
	 * @param tamanoEmpresa tamaño de la empresa
	 */
	public ClienteCorporativo( String identificador, String nombre, String nombreEmpresa, String tamanoEmpresa )
	{
		super( identificador, nombre ); // si la clase Cliente tiene esa firma
		this.nombreEmpresa = nombreEmpresa;
		this.tamanoEmpresa = tamanoEmpresa;
	}

	/* Getters y setters */
	public String getNombreEmpresa( )
	{
		return nombreEmpresa;
	}

	public void setNombreEmpresa( String nombreEmpresa )
	{
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getTamanoEmpresa( )
	{
		return tamanoEmpresa;
	}

	public void setTamanoEmpresa( String tamanoEmpresa )
	{
		this.tamanoEmpresa = tamanoEmpresa;
	}

	/**
	 * Crea un nuevo objeto de tipo a partir de un objeto JSON.
	 * Espera que el JSON contenga las claves: nombreEmpresa, tamanoEmpresa, identificador, nombre, tipo
	 *
	 * @param json El objeto JSON con los datos.
	 * @return ClienteCorporativo construido
	 */
	public static ClienteCorporativo cargarDesdeJSON( JSONObject json )
	{
		ClienteCorporativo c = new ClienteCorporativo( );
		if( json.has( "identificador" ) )
			c.setIdentificador( json.getString( "identificador" ) );
		if( json.has( "nombre" ) )
			c.setNombre( json.getString( "nombre" ) );
		if( json.has( "nombreEmpresa" ) )
			c.nombreEmpresa = json.getString( "nombreEmpresa" );
		if( json.has( "tamanoEmpresa" ) )
			c.tamanoEmpresa = json.getString( "tamanoEmpresa" );
		return c;
	}

	/**
	 * Salva este objeto de tipo ClienteCorporativo dentro de un
	 * objeto JSONObject para que ese objeto se almacene en un archivo
	 * @return El objeto JSON con toda la información del cliente corporativo
	 */
	public JSONObject salvarEnJSON( )
	{
		JSONObject jobject = new JSONObject( );
		// Asegurarse incluir identificador y nombre del cliente base si existen
		if( getIdentificador( ) != null ) jobject.put( "identificador", getIdentificador( ) );
		if( getNombre( ) != null ) jobject.put( "nombre", getNombre( ) );

		jobject.put( "nombreEmpresa", this.nombreEmpresa );
		jobject.put( "tamanoEmpresa", this.tamanoEmpresa );
		jobject.put( "tipo", CORPORATIVO );
		return jobject;
	}
}
