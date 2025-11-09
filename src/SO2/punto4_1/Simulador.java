package SO2.punto4_1;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//Clase memoria
public class Simulador {

    private int tamanoTotal;
    private int tamanoPagina;
    private int espacioDireccionLogica;
    private int tamanoDirecionesLogicasKB;
    private int numeroPaginaDireccionLogica;
    private int marcosTotales;
    private int offsetBits;
    private int bitsNumeroPagina;
    private ArrayList<EntradaTablaPaginas> entradaTablaPaginas = new ArrayList<>();
    private ArrayList<ResultadoTraduccion> resultados = new ArrayList<>();

    public Simulador(int tamanoTotal, int tamanoPagina, int espacioDireccionLogica) {

        this.tamanoTotal = tamanoTotal;
        this.tamanoPagina = tamanoPagina;
        this.espacioDireccionLogica = espacioDireccionLogica;

        tamanoDirecionesLogicasKB = (int) Math.pow(2, espacioDireccionLogica);
        numeroPaginaDireccionLogica = tamanoDirecionesLogicasKB / tamanoPagina;

        marcosTotales = tamanoTotal / tamanoPagina;

        offsetBits = (int) (Math.log(tamanoPagina) / Math.log(2));
        bitsNumeroPagina = (int) (Math.log(numeroPaginaDireccionLogica) / Math.log(2));

        System.out.println("Espacio de direcciones: " + espacioDireccionLogica + " tamaño en kb " + tamanoDirecionesLogicasKB);
        System.out.println("Tamaño de pagina: " + tamanoPagina);
        System.out.println("Numero de paginas lógicas: " + numeroPaginaDireccionLogica);
        System.out.println("Memoria física: " + tamanoTotal);
        System.out.println("Bits para offset: " + offsetBits);
        System.out.println("Bits para numero de pagina: " + bitsNumeroPagina);

    }

    public void assignarPaginas(ArrayList<EntradaTablaPaginas> entradaTablaPaginas) {
        this.entradaTablaPaginas = entradaTablaPaginas;

        System.out.printf("%-12s %-14s %-10s %-12s %-12s %-111s%n",
                "Página", "Marco Físico", "Presente", "Modificada", "Referenciada", "Timestamp");
        System.out.println("--------------------------------------------------------------------------------");
        for (EntradaTablaPaginas p : this.entradaTablaPaginas) {
            String timestamp = p.getTimestamp()
                    .atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            System.out.printf("%-12d %-14d %-10b %-12b %-12b %-12s%n",
                    p.getNumeroPagina(),
                    p.getMarcoFisico(),
                    p.isPresente(),
                    p.isModificado(),
                    p.isReferenciado(),
                    timestamp);
        }

        int[] direcciones = {
                0x0000, 0x0FFF, 0x1234, 0x2ABC, 0x3500,
                0x4000, 0x5800, 0x7FFF, 0x9200, 0xB500
        };

        resultados = new ArrayList<>();
        for (int direccion : direcciones) {
            resultados.add(traducirDirecciones(direccion));
        }

        verTabla();
    }

    public ResultadoTraduccion traducirDirecciones(int direccionLogica) {

        int numeroDePagina = direccionLogica >> offsetBits;

        int offset = direccionLogica & (tamanoPagina - 1);

        EntradaTablaPaginas entradaTablaPaginas = this.entradaTablaPaginas.get(numeroDePagina);

        String mensaje;

        int marco = 0;
        int physicalAddress = 0;
        if (entradaTablaPaginas.isPresente()) {

            physicalAddress = (entradaTablaPaginas.getMarcoFisico() << offsetBits) | offset;
            marco = entradaTablaPaginas.getMarcoFisico();
            mensaje = "Hit";
        } else {

            mensaje = "Page fault";
        }

        return new ResultadoTraduccion(direccionLogica, numeroDePagina, offset, entradaTablaPaginas.isPresente(), marco, physicalAddress, mensaje);
    }

    private void verTabla() {
        System.out.printf("%-14s %-18s %-14s %-10s %-10s %-14s %-18s %-30s %-20s%n",
                "#", "Dirección Lógica", "Núm. Página", "Offset", "Presente", "Marco Físico",
                "Dirección Física", "Mensaje", "Timestamp");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------");


        for (int i = 0; i < resultados.size(); i++) {
            System.out.printf("%-14d %-18s %-14d %-10s %-10b %-14d %-18s %-30s %-20s%n",
                    i + 1,
                    String.format("0x%04X", resultados.get(i).getDireccionLogica()),
                    resultados.get(i).getNumeroPagina(),
                    String.format("0x%03X", resultados.get(i).getOffset()),
                    resultados.get(i).isPresente(),
                    resultados.get(i).getMarcoFisico(),
                    String.format("0x%04X", resultados.get(i).getDireccionFisica()),
                    resultados.get(i).getMensaje(),
                    resultados.get(i).getTimestamp());
        }

    }

}
