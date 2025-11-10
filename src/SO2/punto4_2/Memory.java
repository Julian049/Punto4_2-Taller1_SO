package SO2.punto4_2;

import java.util.ArrayList;
import java.util.Arrays;

//Clase memoria
public class Memory {

    private final int marcosTotales = 4;
    private Frame[] marcos;
    private ArrayList<Integer> paginas;
    private int posicionPaginaAntigua = 0;
    private int acceso = 1;

    public Memory() {
        marcos = new Frame[marcosTotales];
        paginas = new ArrayList<>(Arrays.asList(
                2, 3, 2, 1, 5, 2, 4, 5, 3, 2, 5, 2, 7, 3, 4, 5, 6, 7, 2, 4
        ));
        inicializarMarcos();
    }

    public void simulador() {
        for (int i = 0; i < paginas.size(); i++) {
            accederPaginaFIFO(paginas.get(i));
        }
    }

    private void inicializarMarcos() {
        for (int i = 0; i < marcos.length; i++) {
            marcos[i] = new Frame(-1);
        }
    }

    private void accederPaginaFIFO(int pagina) {

        String fallo = "";
        String marcoVictima = "-";

        if (!verificarPaginaEnMemoria(pagina)) {
            if (marcosLlenos()) {
                marcoVictima = "Pag " + marcos[posicionPaginaAntigua].getPage() + " (marco " + posicionPaginaAntigua + ")";
                marcos[posicionPaginaAntigua].setPage(pagina);
                aumentarPosicion();
                marcos[posicionPaginaAntigua].updateTimestampAcceso();

            } else {
                for (Frame f : marcos) {
                    if (f.getPage() == -1) {
                        f.setPage(pagina);
                        break;
                    }
                    f.updateTimestampAcceso();
                }

            }

            fallo = "Si";
        }else {
            fallo = "No";
        }

// Encabezado
        System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(8) + "+" + "-".repeat(37) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");
        System.out.printf(
                "| %-6s | %-6s | %-35s | %-8s | %-15s |%n",
                "Acceso", "Página", "Estado Memoria (Marcos 0-3)", "¿Fallo?", "Víctima"
        );
        System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(8) + "+" + "-".repeat(37) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");

// Datos
        System.out.printf(
                "| %-6d | %-6d | [%-2s, %-2s, %-2s, %-2s]                | %-8s | %-15s |%n",
                acceso++, pagina,
                (marcos[0].getPage() == -1 ? " " : marcos[0].getPage()),
                (marcos[1].getPage() == -1 ? " " : marcos[1].getPage()),
                (marcos[2].getPage() == -1 ? " " : marcos[2].getPage()),
                (marcos[3].getPage() == -1 ? " " : marcos[3].getPage()),
                fallo,
                marcoVictima
        );

// Línea de cierre (opcional, después de imprimir todos los datos)
        System.out.println("+" + "-".repeat(8) + "+" + "-".repeat(8) + "+" + "-".repeat(37) + "+" + "-".repeat(10) + "+" + "-".repeat(17) + "+");



    }

    private boolean marcosLlenos() {
        boolean out = true;
        for (Frame f : marcos) {
            if (f.getPage() == -1) {
                out = false;
            }
        }
        return out;
    }

    private void aumentarPosicion() {
        if (posicionPaginaAntigua == marcos.length - 1) {
            posicionPaginaAntigua = 0;
        } else {
            posicionPaginaAntigua++;
        }
    }

    private boolean verificarPaginaEnMemoria(int pagina) {
        boolean out = false;
        for (Frame f : marcos) {
            if (f.getPage() == pagina) {
                out = true;
                break;
            }
        }
        return out;
    }
}
