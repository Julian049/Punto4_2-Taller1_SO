package SO2.punto4_1;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ResultadoTraduccion {

    private int direccionLogica;
    private int numeroPagina;
    private int offset;
    private boolean presente;
    private int marcoFisico;
    private int direccionFisica;
    private String mensaje;
    private String timestamp;

    public ResultadoTraduccion(int direccionLogica, int numeroPagina, int offset, boolean presente, int marcoFisico, int direccionFisica, String mensaje) {
        this.direccionLogica = direccionLogica;
        this.numeroPagina = numeroPagina;
        this.offset = offset;
        this.presente = presente;
        this.marcoFisico = marcoFisico;
        this.direccionFisica = direccionFisica;
        this.mensaje = mensaje;
        this.timestamp = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS"));
    }

    public int getDireccionLogica() {
        return direccionLogica;
    }

    public void setDireccionLogica(int direccionLogica) {
        this.direccionLogica = direccionLogica;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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

    public int getDireccionFisica() {
        return direccionFisica;
    }

    public void setDireccionFisica(int direccionFisica) {
        this.direccionFisica = direccionFisica;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
