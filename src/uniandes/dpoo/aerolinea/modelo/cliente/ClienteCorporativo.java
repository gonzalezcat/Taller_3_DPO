package uniandes.dpoo.aerolinea.modelo.cliente;

import org.json.JSONObject;

/**
 * Esta clase se usa para representar a los clientes de la aerolínea que son empresas.
 */
public class ClienteCorporativo extends Cliente
{
    private String nombreEmpresa;
    private String tamanoEmpresa;

    public ClienteCorporativo()
    {
        super();
        this.nombreEmpresa = "";
        this.tamanoEmpresa = "";
    }

    public ClienteCorporativo(String identificador, String nombre, String nombreEmpresa, String tamanoEmpresa)
    {
        super(identificador, nombre);
        this.nombreEmpresa = nombreEmpresa;
        this.tamanoEmpresa = tamanoEmpresa;
    }

    @Override
    public String getTipoCliente()
    {
        return Cliente.CORPORATIVO; // usa la constante del abstracto
    }

    public String getNombreEmpresa()
    {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa)
    {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTamanoEmpresa()
    {
        return tamanoEmpresa;
    }

    public void setTamanoEmpresa(String tamanoEmpresa)
    {
        this.tamanoEmpresa = tamanoEmpresa;
    }

    public static ClienteCorporativo cargarDesdeJSON(JSONObject json)
    {
        ClienteCorporativo c = new ClienteCorporativo();
        if (json.has("identificador"))
            c.setIdentificador(json.getString("identificador"));
        if (json.has("nombre"))
            c.setNombre(json.getString("nombre"));
        if (json.has("nombreEmpresa"))
            c.setNombreEmpresa(json.getString("nombreEmpresa"));
        if (json.has("tamanoEmpresa"))
            c.setTamanoEmpresa(json.getString("tamanoEmpresa"));
        return c;
    }

    public JSONObject salvarEnJSON()
    {
        JSONObject jobject = new JSONObject();
        jobject.put("identificador", getIdentificador());
        jobject.put("nombre", getNombre());
        jobject.put("nombreEmpresa", this.nombreEmpresa);
        jobject.put("tamanoEmpresa", this.tamanoEmpresa);
        jobject.put("tipo", Cliente.CORPORATIVO);
        return jobject;
    }
}
