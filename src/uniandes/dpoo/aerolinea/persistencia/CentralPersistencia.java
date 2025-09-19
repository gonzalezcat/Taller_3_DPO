package uniandes.dpoo.aerolinea.persistencia;

/**
 * Fábrica para obtener componentes de persistencia.
 */
public class CentralPersistencia
{
    public static final String JSON = "JSON";
    public static final String PLAIN = "PlainText";

    /**
     * Retorna una implementación de IPersistenciaAerolinea según el tipo.
     */
    public static IPersistenciaAerolinea getPersistenciaAerolinea(String tipoArchivo) throws TipoInvalidoException
    {
        if (JSON.equals(tipoArchivo))
            return new PersistenciaAerolineaJson();   // debe existir en el proyecto
        else if (PLAIN.equals(tipoArchivo))
            return new PersistenciaAerolineaPlaintext();
        else
            throw new TipoInvalidoException(tipoArchivo);
    }

    /**
     * Retorna una implementación de IPersistenciaTiquetes según el tipo.
     */
    public static IPersistenciaTiquetes getPersistenciaTiquetes(String tipoArchivo) throws TipoInvalidoException
    {
        if (JSON.equals(tipoArchivo))
            return new PersistenciaTiquetesJson();
        else
            throw new TipoInvalidoException(tipoArchivo);
    }
}
