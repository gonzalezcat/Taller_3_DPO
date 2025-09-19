package uniandes.dpoo.aerolinea.tiquetes;

import java.util.HashSet;
import java.util.Set;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

/**
 * Esta clase representa al modulo del sistema que es capaz de generar nuevos tiquetes,
 * asignándole a cada uno un código único.
 */
public class GeneradorTiquetes
{
    /**
     * Un conjunto con los códigos que ya han sido usados anteriormente para otros tiquetes.
     */
    private static Set<String> codigosUsados = new HashSet<>();

    /**
     * Genera un código único de 7 dígitos.
     */
    private static String generarCodigoUnico()
    {
        String codigo;
        int intento = 0;
        synchronized (codigosUsados)
        {
            do
            {
                int numero = (int)(Math.random() * 10_000_000); // entre 0 y 9.999.999
                codigo = String.valueOf(numero);
                while (codigo.length() < 7)
                {
                    codigo = "0" + codigo;
                }
                intento++;
            }
            while (codigosUsados.contains(codigo) && intento < 10000);

            codigosUsados.add(codigo);
        }
        return codigo;
    }

    /**
     * @param vuelo   El vuelo al que está asociado el tiquete
     * @param cliente El cliente que compró el tiquete
     * @param tarifa  El valor que se le cobró al cliente por el tiquete
     * @return El nuevo tiquete, inicializado con un codigo único
     */
    public static Tiquete generarTiquete(Vuelo vuelo, Cliente cliente, int tarifa)
    {
        String codigo = generarCodigoUnico();
        return new Tiquete(codigo, vuelo, cliente, tarifa);
    }

    /**
     *
     * @param unTiquete El tiquete existente
     */
    public static void registrarTiquete(Tiquete unTiquete)
    {
        if (unTiquete == null) return;
        String codigo = unTiquete.getCodigo();
        if (codigo == null) return;
        synchronized (codigosUsados)
        {
            codigosUsados.add(codigo);
        }
    }

    /**

     *
     * @param codigoTiquete El codigo que se quiere consultar
     * @return Retorna true si ya se tenía registrado un tiquete con el codigo dado
     */
    public static boolean validarTiquete(String codigoTiquete)
    {
        if (codigoTiquete == null) return false;
        synchronized (codigosUsados)
        {
            return codigosUsados.contains(codigoTiquete);
        }
    }
}