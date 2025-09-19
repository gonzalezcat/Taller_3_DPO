package uniandes.dpoo.aerolinea.modelo.cliente;
import org.json.JSONObject;

public class ClienteNatural extends Cliente {
	public static final String NATURAL = "NATURAL";

	public ClienteNatural()
	{
		super();
	}

	public ClienteNatural(String identificador, String nombre)
	{
		super(identificador, nombre);
	}

	@Override
	public String getTipoCliente()
	{
		return NATURAL;
	}

	public static ClienteNatural cargarDesdeJSON(JSONObject json)
	{
		ClienteNatural c = new ClienteNatural();
		if (json.has("identificador"))
			c.setIdentificador(json.getString("identificador"));
		if (json.has("nombre"))
			c.setNombre(json.getString("nombre"));
		return c;
	}

	public JSONObject salvarEnJSON()
	{
		JSONObject j = new JSONObject();
		j.put("identificador", getIdentificador());
		j.put("nombre", getNombre());
		j.put("tipo", NATURAL);
		return j;
	}

}
