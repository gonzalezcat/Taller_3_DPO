package uniandes.dpoo.aerolinea.persistencia;

import java.io.IOException;

import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;

public interface IPersistenciaAerolinea {

	/**
     * Carga la información de una aerolínea desde el archivo indicado y actualiza
     * la instancia de Aerolinea que se pasa como parámetro (agregando aviones, rutas, vuelos).
     *
     * @param archivo ruta al archivo de persistencia
     * @param aerolinea instancia a llenar
     * @throws IOException si hay problemas leyendo el archivo
     * @throws InformacionInconsistenteException si hay inconsistencias en el contenido
     */
    public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException;

    /**
     * Salva en el archivo indicado la información (aviones, rutas, vuelos) que
     * está en la instancia de Aerolinea.
     *
     * @param archivo ruta al archivo destino
     * @param aerolinea instancia con la información a salvar
     * @throws IOException si hay problemas escribiendo el archivo
     */
    public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException;
}
