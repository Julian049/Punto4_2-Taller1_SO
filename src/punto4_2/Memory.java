package punto4_2;

import java.util.ArrayList;

//Clase memoria
public class Memory {

    /*
    Se crean variables básicas para el funcionamiento del mismo tales como:
    totalKB -> Tamaño total de la memoria en KB
    frameKBSize -> Tamañol de los frames
    numberFrames -> Cantidad de frames totales dividiendo el tamaño total entre el tamaño de los marcos
    physicalAddress -> Número que indica las direcciones físicas para asignar a los slots de la memoria
    logicalAddress -> Número que indica las direcciones lógicas para asignar a las páginas
    frames -> Arreglo de todos los marcos creados
    pages -> Lista de páginas por asignar a marcos
    processes -> Lista de todos los procesos independientemente de su estado
     */
    private final int totalKBSize;
    private final int frameKBSize;
    private final int numberFrames;
    private int physicalAddress;
    private int logicalAddress;
    private final Frame[] frames;
    private final ArrayList<Page> pages = new ArrayList<>();
    private ArrayList<Process> processes = new ArrayList<>();

    public Memory(int frameKBSize, int totalKBSize) {
        this.frameKBSize = frameKBSize;
        this.totalKBSize = totalKBSize;

        //Calculo del numero de frames totales de la memoria
        numberFrames = totalKBSize / frameKBSize;

        //Instancia frames con un tamaño fijo de acuerdo a la cantidad de marcos
        frames = new Frame[numberFrames];

        //Inicio de direcciones físicas y lógicas
        physicalAddress = 0;
        logicalAddress = 2000;

        //Creación de todos los marcos de la memoria
        fillFrames();
    }

    /*
    Se crean los marcos necesarios de acuerdo al número total de marcos
    Se crean con páginas nulas (aún no se asignan), se les asigna un número y se les pasa el número actual de dirección física
    para que la memoria sepa en qué dirección están
     */
    private void fillFrames() {
        for (int i = 0; i < numberFrames; i++) {
            Frame frame = new Frame(null, i, frameKBSize, physicalAddress);
            frames[i] = frame;
            physicalAddress += frameKBSize;
        }
    }

    /*
    Se asignan las páginas a un proceso, además pasa ha estado READY, ya está listo para entrar a la memoria y ser ejecutado
    Se calcula la cantidad de páginas que le proceso va a tener redondeando hacia arriba, por ejemplo: 2.3 ≈ 3
    Se les asigna un proceso a la página, un número de página y su dirección logica
     */
    public void assignPageToProcess(Process process) {
        process.setState(State.READY);
        System.out.println("Estado actual del " + process.getName() + " " + process.getState());
        int totalPage = (int) Math.ceil((double) process.getSize() / frameKBSize);
        for (int i = 0; i < totalPage; i++) {
            Page page = new Page(i, process, frameKBSize);
            for (int j = 0; j < page.getLogicalAddresses().length; j++) {
                page.getLogicalAddresses()[j] = logicalAddress;
                logicalAddress++;
            }
            pages.add(page);
        }
    }

    public void runMemorySimulator() {

        System.out.println("\n======SIMULACIÓN======\n \n");

        System.out.println("Espacio total de memoria " + totalKBSize + " Kb");
        System.out.println("Numero total de frames: " + numberFrames);
        System.out.println("Tamaño del frame: " + frameKBSize + " Kb");

        System.out.println("Frames disponibles: " + countFreeFrames());

        showMemory();

        assignFrames(null);
        setExecutingProcess();

        System.out.println("Frames disponibles: " + countFreeFrames());
        showMemory();

        showPageTableProcess();

        System.out.println("┌─────────────────────────────┐");
        System.out.println("│   Liberamos el proceso 1    │");
        System.out.println("└─────────────────────────────┘");

        recoverMemory(processes.getFirst());
        processes.getFirst().setState(State.TERMINATED);

        System.out.println("Frames disponibles: " + countFreeFrames());
        showMemory();

        System.out.println("┌─────────────────────────────┐");
        System.out.println("│   Añadimos un proceso 4     │");
        System.out.println("└─────────────────────────────┘");

        Process process4 = new Process("Proceso 4", 18);
        processes.add(process4);
        assignFrames(process4);

        System.out.println("Frames disponibles: " + countFreeFrames());
        showMemory();

        Process process5 = new Process("Proceso 5", 18);
        processes.add(process5);
        assignFrames(process5);

        System.out.println("Frames disponibles: " + countFreeFrames());
        showMemory();

        showPageTableProcess();

        System.out.println("======ESTADOS DE LOS PROCESOS======== \n");

        for (Process process : processes) {
            System.out.println("Estado actual del " + process.getName() + " " + process.getState());
        }

        mapDirections();
    }

    //Retorna cuantos frames hay libres
    private int countFreeFrames() {
        int count = 0;
        for (int i = 0; i < numberFrames; i++) {
            if (frames[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

    //Muestra el estado de la memoria, muestra todos los marcos junto con su estado en la memoria,
    // en caso de que el marco este ocupado se muestra el proceso que lo esta ocupando y el estado del mismo
    // al final muestra cuantos marcos quedaron libres después del llamado del método

    private void showMemory() {
        System.out.println("\n=== ESTADO DE LA MEMORIA ===");
        System.out.println("┌───────────────────────────────────────────┐");
        System.out.println("│        MARCOS DE MEMORIA                  │");
        System.out.println("├───────────────────────────────────────────┤");

        for (int i = 0; i < frames.length; i++) {
            Frame currentFrame = frames[i];
            String frameStatus;
            String processInfo;
            String processState;

            if (currentFrame.isAvailable()) {
                frameStatus = "LIBRE";
                processInfo = "---";
                processState = "---";
            } else {
                frameStatus = "OCUPADO";
                if (currentFrame.getPage() != null && currentFrame.getPage().getProcess() != null) {
                    processInfo = currentFrame.getPage().getProcess().getName();
                    processState = currentFrame.getPage().getProcess().getState().toString();
                } else {
                    processInfo = "Sin proceso";
                    processState = "---";
                }
            }

            System.out.printf("│ Frame %d │ %-8s │ %-10s │ %-10s │%n",
                    (i), frameStatus, processInfo, processState);
        }

        System.out.println("└───────────────────────────────────────────┘");
        System.out.println("Frames libres: " + countFreeFrames() + "/" + numberFrames);
        System.out.println();
    }

    //Cuando un proceso termina, el proceso sale de la memoria y por consecuente se libera la memoria que estaba usando
    //Se deja disponible los marcos en los que estaba el proceso y se liberan las páginas
    private void recoverMemory(Process process) {
        for (int i = 0; i < numberFrames; i++) {
            if (!frames[i].isAvailable() && frames[i].getPage().getProcess() == process) {
                frames[i].setPage(null);
                frames[i].setAvailable(true);
            }
        }
    }

    //Asigna los marcos a los procesos
    //Busca desde el primer marco hasta el último aquellos que estén disponibles para asignar las páginas
    private void assignFrames(Process process) {

        //Si el proceso es nulo se procede a asignar marcos para todas las páginas disponibles
        // Porque esto solo pasa al inicio del programa cuando se crean varios procesos antes de lanzar el simulador
        // Al final las páginas liberan porque ya están en la memoria principal
        if (process == null) {
            for (int i = 0; i < pages.size(); i++) {
                if (frames[i].isAvailable()) {
                    frames[i].setPage(pages.get(i));
                    frames[i].setAvailable(false);
                }
            }
            pages.clear();
        } else {
            assignPageToProcess(process);
            //Si el número de páginas que necesita el proceso es mayor a los marcos disponibles,
            // no se puede asignar el proceso a la memoria y queda en el estado esperando hasta que algún proceso termine la ejecución
            // y pueda entrar
            if (pages.size() <= countFreeFrames()) {
                for (int i = 0; i < pages.size(); i++) {
                    for (int j = 0; i < frames.length; j++) {
                        if (frames[j].isAvailable()) {
                            frames[j].setPage(pages.get(i));
                            frames[j].setAvailable(false);
                            break;
                        }
                    }
                }
                process.setState(State.EXECUTING);
            } else {
                process.setState(State.WAITING_FOR_RESOURCE);
                System.out.println("ERROR: No es posible añadir el proceso a la memoria porque no hay suficientes frames libres");
            }
        }
    }

    //Asigna el estado EXECUTING a los procesos
    private void setExecutingProcess() {
        for (Process process : processes) {
            process.setState(State.EXECUTING);
        }
    }

    //Muestra la tabla de paginas de todos los procesos
    private void showPageTableProcess() {
        for (Process proc : processes) {
            System.out.println("\n┌───────────────────────────────┐");
            System.out.println("│    " + proc.getName() + "                  │");
            System.out.println("├───────────────┬───────────────┤");
            System.out.println("│    FRAME      │    PAGINA     │");
            System.out.println("├───────────────┼───────────────┤");
            for (int i = 0; i < frames.length; i++) {
                Frame frame = frames[i];
                if (!frame.isAvailable()) {
                    Page page = frame.getPage();
                    if (page.getProcess() == proc) {
                        System.out.printf("│      %-9d│      %-9d│%n", i, page.getNumberPage());
                    }
                }
            }
            System.out.println("└───────────────┴───────────────┘");
        }
    }

    //Muestra el mapeo de direcciones lógicas y físicas por proceso
    private void mapDirections() {
        System.out.println("\n=== MAPEO DE DIRECCIONES FÍSICAS ===");
        System.out.printf("%-15s%-8s%-20s%-10s%-20s%n", "Proceso", "Frame", "Dirección física", "Pagina", "Dirección lógica");

        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            Page page = frame.getPage();

            int[] physicalAddresses = frame.getPhysicalAddresses();
            if (page != null) {
                int[] logicalAddresses = page.getLogicalAddresses();
                String processName = page.getProcess().getName();

                System.out.printf("%-15s%-8d%-20d%-10d%-20d%n",
                        processName,
                        i,
                        physicalAddresses[0],
                        page.getNumberPage(),
                        logicalAddresses[0]);

                int maxLength = Math.max(physicalAddresses.length, logicalAddresses.length);
                for (int j = 1; j < maxLength; j++) {
                    String physAddr = (j < physicalAddresses.length) ? String.valueOf(physicalAddresses[j]) : "";
                    String logAddr = (j < logicalAddresses.length) ? String.valueOf(logicalAddresses[j]) : "";
                    System.out.printf("%-15s%-8s%-20s%-10s%-20s%n", "", "", physAddr, "", logAddr);
                }
            } else {
                System.out.printf("%-15s%-8d%-20d%-10s%-20s%n", "N/A", i, physicalAddresses[0], "N/A", "N/A");
                for (int j = 1; j < physicalAddresses.length; j++) {
                    System.out.printf("%-15s%-8s%-20d%-10s%-20s%n", "", "", physicalAddresses[j], "", "");
                }
            }
        }
        System.out.println();
    }

    //Recupera los procesos
    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
    }
}
