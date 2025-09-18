package punto4_2;

//Clase pagina
public class Page {
    private int numberPage;
    private Process process;
    private int[] logicalAddresses;

    public Page(int numberPage, Process process,int pageSize) {
        this.numberPage = numberPage;
        this.process = process;
        this.logicalAddresses = new int[pageSize];
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int[] getLogicalAddresses() {
        return logicalAddresses;
    }

    public void setLogicalAddresses(int[] logicalAddresses) {
        this.logicalAddresses = logicalAddresses;
    }
}
