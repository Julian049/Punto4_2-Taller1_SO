package punto4_2;

import java.util.ArrayList;

//Clase memoria
public class Memory {

    /*
    Se crean variables basicas para el funcionamiento del mismo tales como:
    totalKB -> Tamaño total de la memoria en KB
    frameKBSize -> Tamañol de los frames
    numberFrames -> Cantidad de frames totales calculado diviendo el tamaño total entre el tamaño de los marcos
    physicalAddress -> Numero que indica las direcciones fisicas para asignar a los slots de la memoria
    logicalAddress -> Numero que indica las direcciones logicas para asignar a las paginas
    frames -> Arreglo de todos los marcos creados
    pages -> Lista de paginas por asignar a marcos
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

        //Instacia frames con un tamaño fijo de acuerdo a la cantidad de marcos
        frames = new Frame[numberFrames];

        //Inicio de direcciones fisicas y logicas
        physicalAddress = 0;
        logicalAddress = 2000;

        //Creacion de todos los marcos de la memoria
        fillFrames();
    }

    /*
    Se crean los marcos necesarios de acuerdo al numero total de marcos
    Se crean con paginas nulas (aun no se aginan), se les asigna un numero y se les pasa el numero actual de direccion fisica
    para que la memoria sepa en que direccion estan
     */
    private void fillFrames() {
        for (int i = 0; i < numberFrames; i++) {
            Frame frame = new Frame(null, i, frameKBSize, physicalAddress);
            frames[i] = frame;
            physicalAddress += frameKBSize;
        }
    }

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

        System.out.println("\n======SIMULACION======\n \n");

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

        recoverMemory(processes.get(0));
        processes.get(0).setState(State.TERMINATED);

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

        for (int i = 0; i < processes.size(); i++) {
            System.out.println("Estado actual del " + processes.get(i).getName() + " " + processes.get(i).getState());
        }

        mapDirections1();
    }

    private int countFreeFrames() {
        int count = 0;
        for (int i = 0; i < numberFrames; i++) {
            if (frames[i].isAvailable()) {
                count++;
            }
        }
        return count;
    }

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


    private void recoverMemory(Process process) {
        for (int i = 0; i < numberFrames; i++) {
            if (!frames[i].isAvailable() && frames[i].getPage().getProcess() == process) {
                frames[i].setPage(null);
                frames[i].setAvailable(true);
            }
        }
    }

    private void assignFrames(Process process) {

        if (process == null) {
            for (int i = 0; i < pages.size(); i++) {
                if (frames[i].isAvailable()) {
                    frames[i].setPage(pages.get(i));
                    frames[i].setAvailable(false);
                    System.out.println(frames[i].getPhysicalAddresses());
                }
            }
            pages.clear();
        } else {
            assignPageToProcess(process);
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

    private void setExecutingProcess() {
        for (Process process : processes) {
            process.setState(State.EXECUTING);
        }
    }

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

    private void mapDirections() {

    }

    private void mapDirections1() {
        System.out.println("\n=== MAPEO DE DIRECCIONES FÍSICAS ===");
        System.out.printf("%-15s%-8s%-20s%-10s%-20s%n", "Proceso", "Frame", "Direccion fisica", "Pagina", "Direccion logica");

        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            Page page = frame.getPage();

            if (page != null) {
                int[] physicalAddresses = frame.getPhysicalAddresses();
                int[] logicalAddresses = page.getLogicalAddresses();
                String processName = page.getProcess().getName();

                // Primera fila con toda la información
                System.out.printf("%-15s%-8d%-20d%-10d%-20d%n",
                        processName,
                        i,
                        physicalAddresses[0],
                        page.getNumberPage(),
                        logicalAddresses[0]);

                // Filas adicionales solo con direcciones
                int maxLength = Math.max(physicalAddresses.length, logicalAddresses.length);
                for (int j = 1; j < maxLength; j++) {
                    String physAddr = (j < physicalAddresses.length) ? String.valueOf(physicalAddresses[j]) : "";
                    String logAddr = (j < logicalAddresses.length) ? String.valueOf(logicalAddresses[j]) : "";
                    System.out.printf("%-15s%-8s%-20s%-10s%-20s%n", "", "", physAddr, "", logAddr);
                }
            } else {
                // Si no hay página asignada
                int[] physicalAddresses = frame.getPhysicalAddresses();
                System.out.printf("%-15s%-8d%-20d%-10s%-20s%n", "N/A", i, physicalAddresses[0], "N/A", "N/A");
                for (int j = 1; j < physicalAddresses.length; j++) {
                    System.out.printf("%-15s%-8s%-20d%-10s%-20s%n", "", "", physicalAddresses[j], "", "");
                }
            }
        }
        System.out.println();
    }


    public ArrayList<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(ArrayList<Process> processes) {
        this.processes = processes;
    }
}
