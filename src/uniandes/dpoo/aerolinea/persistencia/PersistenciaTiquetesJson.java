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

        cargarClientesDesdeJSON(aerolinea, raiz.getJSONArray("clientes"));
        cargarTiquetesDesdeJSON(aerolinea, raiz.getJSONArray("tiquetes"));
    }

    @Override
    public void salvarTiquetes(String archivo, Aerolinea aerolinea) throws IOException
    {
        JSONObject jobject = new JSONObject();

        salvarClientesEnJSON(aerolinea, jobject);
        salvarTiquetesEnJSON(aerolinea, jobject);

        try (PrintWriter pw = new PrintWriter(archivo))
        {
            jobject.write(pw, 2, 0);
        }
    }

    // ------------------- MÉTODOS PRIVADOS ---------------------

    private void cargarClientesDesdeJSON(Aerolinea aerolinea, JSONArray jClientes) throws ClienteRepetidoException
    {
        for (int i = 0; i < jClientes.length(); i++)
        {
            JSONObject cliente = jClientes.getJSONObject(i);
            String tipoCliente = cliente.getString(TIPO);
            Cliente nuevoCliente;

            if (ClienteNatural.NATURAL.equals(tipoCliente))
            {
                String identificador = cliente.getString(IDENTIFICADOR);
                String nombre = cliente.getString(NOMBRE);
                nuevoCliente = new ClienteNatural(identificador, nombre);
            }
            else
            {
                nuevoCliente = ClienteCorporativo.cargarDesdeJSON(cliente);
            }

            if (!aerolinea.existeCliente(nuevoCliente.getIdentificador()))
                aerolinea.agregarCliente(nuevoCliente);
            else
                throw new ClienteRepetidoException(nuevoCliente.getTipoCliente(), nuevoCliente.getIdentificador());
        }
    }

    private void salvarClientesEnJSON(Aerolinea aerolinea, JSONObject jobject)
    {
        JSONArray jClientes = new JSONArray();
        for (Cliente cliente : aerolinea.getClientes())
        {
            if (ClienteNatural.NATURAL.equals(cliente.getTipoCliente()))
            {
                JSONObject jCliente = new JSONObject();
                jCliente.put(IDENTIFICADOR, cliente.getIdentificador());
                jCliente.put(NOMBRE, cliente.getNombre());
                jCliente.put(TIPO, ClienteNatural.NATURAL);
                jClientes.put(jCliente);
            }
            else
            {
                ClienteCorporativo cc = (ClienteCorporativo) cliente;
                jClientes.put(cc.salvarEnJSON());
            }
        }
        jobject.put("clientes", jClientes);
    }

    private void cargarTiquetesDesdeJSON(Aerolinea aerolinea, JSONArray jTiquetes) throws InformacionInconsistenteTiqueteException
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

    private void salvarTiquetesEnJSON(Aerolinea aerolinea, JSONObject jobject)
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
