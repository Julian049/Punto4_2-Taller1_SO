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
    private int hits = 0;
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

        System.out.println("Espacio de direcciones: " + espacioDireccionLogica + " tamaño en bytes: " + tamanoDirecionesLogicasKB);
        System.out.println("Tamaño de pagina: " + tamanoPagina + "B");
        System.out.println("Numero de paginas lógicas: " + numeroPaginaDireccionLogica);
        System.out.println("Memoria física: " + tamanoTotal + "B");
        System.out.println("Bits para offset: " + offsetBits);
        System.out.println("Bits para numero de pagina: " + bitsNumeroPagina);

    }

    public void asignarPaginas(ArrayList<EntradaTablaPaginas> entradaTablaPaginas) {
        this.entradaTablaPaginas = entradaTablaPaginas;
        System.out.println("Tabla de paginas \n");

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

        System.out.println("\n\n");
    }

    public void ejecutarSimulador(){
        int[] direcciones = {
                0x0000, 0x0FFF, 0x1234, 0x2ABC, 0x3500,
                0x4000, 0x5800, 0x7FFF, 0x9200, 0xB500
        };

        resultados = new ArrayList<>();
        for (int direccion : direcciones) {
            resultados.add(traducirDirecciones(direccion));
        }

        verTabla();

        System.out.println("Hit Rate: ");
        double hitRate = ((double) hits / direcciones.length) * 100;

        System.out.println("El hit rate para esta simulacion teniendo en cuenta que hay " + direcciones.length + " direcciones, es del " + hitRate + "%");


        System.out.println("Calculo manual para la direccion 1: " + direcciones[0]);
        mostrarCalculoManual(direcciones[0]);

        System.out.println("Calculo manual para la direccion 4: " + direcciones[3]);
        mostrarCalculoManual(direcciones[3]);

        System.out.println("Calculo manual para la direccion 7: " + direcciones[6]);
        mostrarCalculoManual(direcciones[6]);
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
            hits++;
        } else {

            mensaje = "Page fault";
        }

        return new ResultadoTraduccion(direccionLogica, numeroDePagina, offset, entradaTablaPaginas.isPresente(), marco, physicalAddress, mensaje);
    }

    public void mostrarCalculoManual(int dirLogica) {
        System.out.println("Inciando calculo manual para la direccion logica: " + String.format("0x%04X", dirLogica) + "\n");

        System.out.println("Convertimos el numero a un numero binario de " + espacioDireccionLogica + " bits");
        String binario = String.format("%16s", Integer.toBinaryString(dirLogica)).replaceAll(" ", "0");
        System.out.println(String.format("0x%04X", dirLogica) + " = " + binario + "\n");

        System.out.println("En este caso, como el numero de bits de la direccion logica son " + espacioDireccionLogica + " bits, tenemos un offset de: " + offsetBits + " bits y la pagina ocuparia los " + bitsNumeroPagina + " bits restantes" + "\n");
        System.out.println("Extraemos los bits offset: (bits menos significativos)");
        String offset = binario.substring(bitsNumeroPagina);
        System.out.println("Offset: " + offset + "\n");
        System.out.println("Extraemos los bits de la pagina: (bits mas significativos)");
        String pagina = binario.substring(0, bitsNumeroPagina);
        System.out.println("Pagina: " + pagina + "\n");

        System.out.println("Con esta informacion podemos obtener la informacion en hexadecimal y decimal para la pagina: " + "\n");
        System.out.println("Direccion logica " + String.format("0x%04X", dirLogica));
        System.out.println("Pagina: " + Integer.toBinaryString(Integer.parseInt(pagina)));
        int valor = Integer.parseInt(offset, 2);
        String hex = Integer.toHexString(valor).toUpperCase();
        System.out.println("Offset: " + "0x" + hex + "\n" + "\n");

        System.out.println("Para calcular la direccion fisica necesitamos consultar en la tabla de paginas, el marco a la que pertenece la pagina");
        int marco = entradaTablaPaginas.get(Integer.parseInt(pagina, 2)).getMarcoFisico();
        boolean presente = entradaTablaPaginas.get(Integer.parseInt(pagina, 2)).isPresente();

        System.out.println("Ahora revisamos si el marco se encuentra presente en la RAM");
        System.out.println("-----------------------------------------");
        if (presente) {
            System.out.println("El marco se encuentra presente en la memoria RAM" + "\n");
            System.out.println("Marco: " + marco);

            System.out.println("Tenemos el marco " + marco + ", este lo convertimos a un binario y lo desplazamos " + offsetBits + " a la izquierda");
            System.out.println(String.format("%16s", Integer.toBinaryString(marco)).replaceAll(" ", "0") + "\n");

            System.out.println("Aplicamos marco << bitsOffset (" + offsetBits + ")");

            int dir1 = marco << 12;
            System.out.println("Marco con los bits desplazados a la izquierda: " + String.format("%16s", Integer.toBinaryString(dir1)).replaceAll(" ", "0") + "\n");
            System.out.println("Luego con un operador OR operamos el marco desplazado con el offset original (marcodesplazado | offset)");

            int dir2 = dir1 | Integer.parseInt(offset, 2);
            System.out.println(String.format("%16s", Integer.toBinaryString(dir1)).replaceAll(" ", "0") + " | "
                    + String.format("%16s", offset).replaceAll(" ", "0") + " = " + String.format("%16s", Integer.toBinaryString(dir2)).replaceAll(" ", "0") + "\n");


            System.out.println("Y obtenemos la direccion fisica: " + "0x" + Integer.toHexString(dir2).toUpperCase());
            System.out.println("Hit");
        } else {
            System.out.println("El marco NO se encuentra presente en la memoria RAM");
            System.out.println("Marco: -");
            System.out.println("Dir fisica: -");
            System.out.println("Page fault");
        }


    }

    private void verTabla() {
        System.out.println("Tabla de traducciones \n");
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

        System.out.println("\n\n");

    }

}
