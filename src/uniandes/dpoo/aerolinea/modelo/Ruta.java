package uniandes.dpoo.aerolinea.modelo;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

/**
 * Esta clase tiene la información de una ruta entre dos aeropuertos que cubre una aerolínea.
 */
public class Ruta
{
    private String codigoRuta;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private String horaSalida;
    private String horaLlegada;

    public Ruta(Aeropuerto origen, Aeropuerto destino, String horaSalida, String horaLlegada, String codigoRuta)
    {
        this.origen = origen;
        this.destino = destino;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.codigoRuta = codigoRuta;
    }

    // ---------------- Getters ----------------

    public String getCodigoRuta()
    {
        return codigoRuta;
    }

    public Aeropuerto getOrigen()
    {
        return origen;
    }

    public Aeropuerto getDestino()
    {
        return destino;
    }

    public String getHoraSalida()
    {
        return horaSalida;
    }

    public String getHoraLlegada()
    {
        return horaLlegada;
    }

    // ---------------- Utilidades ----------------

    public static int getMinutos(String horaCompleta)
    {
        if (horaCompleta == null || horaCompleta.trim().isEmpty()) return 0;
        String s = horaCompleta.trim();
        if (s.length() <= 2) return Integer.parseInt(s);
        String minutosStr = s.substring(s.length() - 2);
        return Integer.parseInt(minutosStr);
    }

    public static int getHoras(String horaCompleta)
    {
        if (horaCompleta == null || horaCompleta.trim().isEmpty()) return 0;
        String s = horaCompleta.trim();
        return Integer.parseInt(s) / 100;
    }

    // ---------------- Tarifa ----------------

    /**
     * Calcula la tarifa para un cliente en un vuelo en esta ruta.
     * Aquí puedes ajustar la lógica según las reglas del PDF.
     */
    public int calcularTarifa(Cliente cliente, Vuelo vuelo)
    {
        int base = 500; // valor base, adáptalo si el PDF da otra fórmula

        // Temporada alta = junio(6)-agosto(8), diciembre(12)
        int mes = obtenerMesDeFecha(vuelo.getFecha());
        boolean temporadaAlta = (mes >= 6 && mes <= 8) || (mes == 12);

        if (temporadaAlta)
            base *= 2; // temporada alta = tarifa doble

        // Ejemplo de ajuste para corporativos (puedes cambiar según reglas reales del PDF)
        if ("CORPORATIVO".equals(cliente.getTipoCliente()))
            base *= 0.9; // descuento del 10%

        return base;
    }

    private int obtenerMesDeFecha(String fecha)
    {
        // Asumimos formato yyyy-mm-dd
        try
        {
            String[] partes = fecha.split("-");
            return Integer.parseInt(partes[1]);
        }
        catch (Exception e)
        {
            return 1; // fallback = enero
        }
    }

    // ---------------- Overrides ----------------

    @Override
    public String toString()
    {
        return String.format("Ruta[%s: %s -> %s, sale %s llega %s]",
                codigoRuta,
                origen == null ? "??" : origen.getCodigo(),
                destino == null ? "??" : destino.getCodigo(),
                horaSalida, horaLlegada);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || !(o instanceof Ruta)) return false;
        Ruta r = (Ruta) o;
        return this.codigoRuta != null && this.codigoRuta.equals(r.getCodigoRuta());
    }

    @Override
    public int hashCode()
    {
        return (codigoRuta == null) ? 0 : codigoRuta.hashCode();
    }
}
