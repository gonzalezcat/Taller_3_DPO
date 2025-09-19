package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;

/**
 * Persistencia en formato JSON para la información "estable" de la aerolínea:
 * aviones, rutas y vuelos.
 */
public class PersistenciaAerolineaJson implements IPersistenciaAerolinea
{
    private static final String AVIONES = "aviones";
    private static final String RUTAS = "rutas";
    private static final String VUELOS = "vuelos";

    private static final String NOMBRE_AVION = "nombre";
    private static final String CAPACIDAD = "capacidad";

    private static final String CODIGO_RUTA = "codigoRuta";
    private static final String ORIGEN = "origen";
    private static final String DESTINO = "destino";

    private static final String FECHA = "fecha";
    private static final String AVION = "avion";

    @Override
    public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException
    {
        String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
        JSONObject raiz = new JSONObject(jsonCompleto);

        // Aviones
        JSONArray jAviones = raiz.getJSONArray(AVIONES);
        for (int i = 0; i < jAviones.length(); i++)
        {
            JSONObject jAvion = jAviones.getJSONObject(i);
            String nombre = jAvion.getString(NOMBRE_AVION);
            int capacidad = jAvion.getInt(CAPACIDAD);
            Avion avion = new Avion(nombre, capacidad);
            aerolinea.agregarAvion(avion);
        }

        // Rutas
        JSONArray jRutas = raiz.getJSONArray(RUTAS);
        for (int i = 0; i < jRutas.length(); i++)
        {
            JSONObject jRuta = jRutas.getJSONObject(i);
            String codigo = jRuta.getString(CODIGO_RUTA);
            String origen = jRuta.getString(ORIGEN);
            String destino = jRuta.getString(DESTINO);
            Ruta ruta = new Ruta(codigo, origen, destino);
            aerolinea.agregarRuta(ruta);
        }

        // Vuelos
        JSONArray jVuelos = raiz.getJSONArray(VUELOS);
        for (int i = 0; i < jVuelos.length(); i++)
        {
            JSONObject jVuelo = jVuelos.getJSONObject(i);
            String codigoRuta = jVuelo.getString(CODIGO_RUTA);
            String fecha = jVuelo.getString(FECHA);
            String nombreAvion = jVuelo.getString(AVION);

            Ruta ruta = aerolinea.getRuta(codigoRuta);
            Avion avion = aerolinea.getAvion(nombreAvion);

            if (ruta == null || avion == null)
                throw new InformacionInconsistenteException("Vuelo con ruta o avión inexistente");

            Vuelo vuelo = new Vuelo(ruta, fecha, avion);
            aerolinea.agregarVuelo(vuelo);
        }
    }

    @Override
    public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException
    {
        JSONObject raiz = new JSONObject();

        // Aviones
        JSONArray jAviones = new JSONArray();
        for (Avion avion : aerolinea.getAviones())
        {
            JSONObject jAvion = new JSONObject();
            jAvion.put(NOMBRE_AVION, avion.getNombre());
            jAvion.put(CAPACIDAD, avion.getCapacidad());
            jAviones.put(jAvion);
        }
        raiz.put(AVIONES, jAviones);

        // Rutas
        JSONArray jRutas = new JSONArray();
        for (Ruta ruta : aerolinea.getRutas())
        {
            JSONObject jRuta = new JSONObject();
            jRuta.put(CODIGO_RUTA, ruta.getCodigoRuta());
            jRuta.put(ORIGEN, ruta.getOrigen());
            jRuta.put(DESTINO, ruta.getDestino());
            jRutas.put(jRuta);
        }
        raiz.put(RUTAS, jRutas);

        // Vuelos
        JSONArray jVuelos = new JSONArray();
        for (Vuelo vuelo : aerolinea.getVuelos())
        {
            JSONObject jVuelo = new JSONObject();
            jVuelo.put(CODIGO_RUTA, vuelo.getRuta().getCodigoRuta());
            jVuelo.put(FECHA, vuelo.getFecha());
            jVuelo.put(AVION, vuelo.getAvion().getNombre());
            jVuelos.put(jVuelo);
        }
        raiz.put(VUELOS, jVuelos);

        try (PrintWriter pw = new PrintWriter(archivo))
        {
            raiz.write(pw, 2, 0);
        }
    }
}
