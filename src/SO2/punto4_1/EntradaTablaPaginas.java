package SO2.punto4_1;

import java.time.Instant;

public class EntradaTablaPaginas {
    private int numeroPagina;
    private int marcoFisico;
    private boolean presente;
    private boolean modificado;
    private boolean referenciado;
    private Instant timestamp;

    public EntradaTablaPaginas(int numeroPagina, int marcoFisico, boolean presente, boolean modificado, boolean referenciado) {
        this.numeroPagina = numeroPagina;
        this.marcoFisico = marcoFisico;
        this.presente = presente;
        this.modificado = modificado;
        this.referenciado = referenciado;
        this.timestamp = Instant.now();
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public int getMarcoFisico() {
        return marcoFisico;
    }

    public void setMarcoFisico(int marcoFisico) {
        this.marcoFisico = marcoFisico;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    public boolean isReferenciado() {
        return referenciado;
    }

    public void setReferenciado(boolean referenciado) {
        this.referenciado = referenciado;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
