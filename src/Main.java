import punto4_2.Memory;
import punto4_2.Process;

public class Main {
    public static void main(String[] args) {
        System.out.println("SIMULACION CON PAGINAS Y FRAMES\n \n");

        //Instancia la clase memoria
        //Asigna un tamaño total a la misma de 99 KB y marcos de 3 Kb
        Memory memory = new Memory(3, 99);

        //Crea 3 procesos con tamaños de 5,30,40
        punto4_2.Process process1 = new punto4_2.Process("Proceso 1", 5);
        punto4_2.Process process2 = new punto4_2.Process("Proceso 2", 30);
        punto4_2.Process process3 = new Process("Proceso 3", 40);
        memory.getProcesses().add(process1);
        memory.getProcesses().add(process2);
        memory.getProcesses().add(process3);

        System.out.println("======ESTADOS DE LOS PROCESOS======== \n");

        //Muestra los estados actuales de los procesos
        for (int i = 0; i < memory.getProcesses().size(); i++) {
            System.out.println("Estado actual del " + memory.getProcesses().get(i).getName() + " " + memory.getProcesses().get(i).getState());
        }

        System.out.println("\n \n");

        System.out.println("======ESTADOS DE LOS PROCESOS======== \n");

        //Asigna paginas a los procesos
        memory.assignPageToProcess(process1);
        memory.assignPageToProcess(process2);
        memory.assignPageToProcess(process3);

        //Ejecuta el simulador
        memory.runMemorySimulator();
    }
}