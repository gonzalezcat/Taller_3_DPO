package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteTiqueteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaTiquetesJson implements IPersistenciaTiquetes
{
    private static final String IDENTIFICADOR = "identificador";
    private static final String NOMBRE = "nombre";
    private static final String TIPO = "tipo";

    private static final String CLIENTE = "cliente";
    private static final String USADO = "usado";
    private static final String TARIFA = "tarifa";
    private static final String CODIGO_TIQUETE = "codigoTiquete";
    private static final String FECHA = "fecha";
    private static final String CODIGO_RUTA = "codigoRuta";

    @Override
    public void cargarTiquetes(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException
    {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        cargarClientes(aerolinea, raiz.getJSONArray("clientes"));
        cargarTiquetes(aerolinea, raiz.getJSONArray("tiquetes"));
    }

    @Override
    public void salvarTiquetes(String archivo, Aerolinea aerolinea) throws IOException
    {
        JSONObject jobject = new JSONObject();

        salvarClientes(aerolinea, jobject);
        salvarTiquetes(aerolinea, jobject);

        try (PrintWriter pw = new PrintWriter(archivo))
        {
            jobject.write(pw, 2, 0);
        }
    }

    private void cargarClientes(Aerolinea aerolinea, JSONArray jClientes) throws ClienteRepetidoException
    {
        for (int i = 0; i < jClientes.length(); i++)
        {
            JSONObject cliente = jClientes.getJSONObject(i);
            String tipoCliente = cliente.getString(TIPO);
            Cliente nuevoCliente;

            if (Cliente.NATURAL.equals(tipoCliente))
            {
                nuevoCliente = ClienteNatural.cargarDesdeJSON(cliente);
            }
            else if (Cliente.CORPORATIVO.equals(tipoCliente))
            {
                nuevoCliente = ClienteCorporativo.cargarDesdeJSON(cliente);
            }
            else
            {
                throw new IllegalArgumentException("Tipo de cliente desconocido: " + tipoCliente);
            }

            if (!aerolinea.existeCliente(nuevoCliente.getIdentificador()))
                aerolinea.agregarCliente(nuevoCliente);
            else
                throw new ClienteRepetidoException(nuevoCliente.getTipoCliente(), nuevoCliente.getIdentificador());
        }
    }

    private void salvarClientes(Aerolinea aerolinea, JSONObject jobject)
    {
        JSONArray jClientes = new JSONArray();
        for (Cliente cliente : aerolinea.getClientes())
        {
            if (Cliente.NATURAL.equals(cliente.getTipoCliente()))
            {
                jClientes.put(((ClienteNatural) cliente).salvarEnJSON());
            }
            else if (Cliente.CORPORATIVO.equals(cliente.getTipoCliente()))
            {
                jClientes.put(((ClienteCorporativo) cliente).salvarEnJSON());
            }
        }
        jobject.put("clientes", jClientes);
    }

    private void cargarTiquetes(Aerolinea aerolinea, JSONArray jTiquetes) throws InformacionInconsistenteTiqueteException
    {
        for (int i = 0; i < jTiquetes.length(); i++)
        {
            JSONObject tiquete = jTiquetes.getJSONObject(i);

            String codigoRuta = tiquete.getString(CODIGO_RUTA);
            Ruta laRuta = aerolinea.getRuta(codigoRuta);
            if (laRuta == null)
                throw new InformacionInconsistenteTiqueteException("ruta", codigoRuta);

            String fechaVuelo = tiquete.getString(FECHA);
            Vuelo elVuelo = aerolinea.getVuelo(codigoRuta, fechaVuelo);
            if (elVuelo == null)
                throw new InformacionInconsistenteTiqueteException("vuelo", codigoRuta + " en " + fechaVuelo);

            String codigoTiquete = tiquete.getString(CODIGO_TIQUETE);
            if (GeneradorTiquetes.validarTiquete(codigoTiquete))
                throw new InformacionInconsistenteTiqueteException("tiquete", codigoTiquete, false);

            int tarifa = tiquete.getInt(TARIFA);
            boolean usado = tiquete.getBoolean(USADO);

            String idCliente = tiquete.getString(CLIENTE);
            Cliente cliente = aerolinea.getCliente(idCliente);
            if (cliente == null)
                throw new InformacionInconsistenteTiqueteException("cliente", idCliente);

            Tiquete nuevoTiquete = new Tiquete(codigoTiquete, elVuelo, cliente, tarifa);
            if (usado) nuevoTiquete.marcarComoUsado();

            GeneradorTiquetes.registrarTiquete(nuevoTiquete);
        }
    }

    private void salvarTiquetes(Aerolinea aerolinea, JSONObject jobject)
    {
        JSONArray jTiquetes = new JSONArray();
        for (Tiquete tiquete : aerolinea.getTiquetes())
        {
            JSONObject jTiquete = new JSONObject();
            jTiquete.put(CODIGO_TIQUETE, tiquete.getCodigo());
            jTiquete.put(CODIGO_RUTA, tiquete.getVuelo().getRuta().getCodigoRuta());
            jTiquete.put(FECHA, tiquete.getVuelo().getFecha());
            jTiquete.put(TARIFA, tiquete.getTarifa());
            jTiquete.put(USADO, tiquete.esUsado());
            jTiquete.put(CLIENTE, tiquete.getCliente().getIdentificador());

            jTiquetes.put(jTiquete);
        }
        jobject.put("tiquetes", jTiquetes);
    }
}
