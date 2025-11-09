package SO2.punto4_1;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("SIMULACION DE TRADUCCIONES DE DIRECCIONES CON MMU\n \n");

        final int LOGIC_ADDRESS_SPACE = 16; // Espacio de direcciones logicas en bits
        final int PAGE_SIZE = 4096; //Tamaño de pagina en bytes
        final int PHYSICAL_SIZE = 32768; //Tamaño de la memoria fisica en bytes

        System.out.println("Inicializando memoria....");
        System.out.println("----------------------");
        Simulador simulador = new Simulador(PHYSICAL_SIZE,PAGE_SIZE,LOGIC_ADDRESS_SPACE);
        System.out.println("Memoria inicializada :)\n");

        System.out.println("Creando paginas...");
        System.out.println("----------------------");
        ArrayList<EntradaTablaPaginas> entradaTablaPaginas = new ArrayList<EntradaTablaPaginas>();
        EntradaTablaPaginas entradaTablaPaginas1 = new EntradaTablaPaginas(0,3,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas1);
        EntradaTablaPaginas entradaTablaPaginas2 = new EntradaTablaPaginas(1,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas2);
        EntradaTablaPaginas entradaTablaPaginas3 = new EntradaTablaPaginas(2,5,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas3);
        EntradaTablaPaginas entradaTablaPaginas4 = new EntradaTablaPaginas(3,1,true,true,false);
        entradaTablaPaginas.add(entradaTablaPaginas4);
        EntradaTablaPaginas entradaTablaPaginas5 = new EntradaTablaPaginas(4,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas5);
        EntradaTablaPaginas entradaTablaPaginas6 = new EntradaTablaPaginas(5,7,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas6);
        EntradaTablaPaginas entradaTablaPaginas7 = new EntradaTablaPaginas(6,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas7);
        EntradaTablaPaginas entradaTablaPaginas8 = new EntradaTablaPaginas(7,2,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas8);
        EntradaTablaPaginas entradaTablaPaginas9 = new EntradaTablaPaginas(8,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas9);
        EntradaTablaPaginas entradaTablaPaginas10 = new EntradaTablaPaginas(9,4,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas10);
        EntradaTablaPaginas entradaTablaPaginas11 = new EntradaTablaPaginas(10,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas11);
        EntradaTablaPaginas entradaTablaPaginas12 = new EntradaTablaPaginas(11,6,true,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas12);
        EntradaTablaPaginas entradaTablaPaginas13 = new EntradaTablaPaginas(12,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas13);
        EntradaTablaPaginas entradaTablaPaginas14 = new EntradaTablaPaginas(13,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas14);
        EntradaTablaPaginas entradaTablaPaginas15 = new EntradaTablaPaginas(14,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas15);
        EntradaTablaPaginas entradaTablaPaginas16 = new EntradaTablaPaginas(15,-1,false,false,false);
        entradaTablaPaginas.add(entradaTablaPaginas16);
        System.out.println("Paginas creadas :)\n");

        System.out.println("Asignando paginas...\n");
        System.out.println("----------------------");
        simulador.assignarPaginas(entradaTablaPaginas);

        simulador.traducirDirecciones(0x3500);

    }
}