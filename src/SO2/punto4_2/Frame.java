package SO2.punto4_2;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

//Clase frame (Marco)
public class Frame {
    private int page;
    private String timestampCarga;
    private String timestampAcceso;
    private boolean fallo;

    public Frame(int page) {
        if (page < 0) {
            this.page = -1;
        } else {
            this.page = page;
        }
        timestampCarga = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS"));
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTimestampCarga() {
        return timestampCarga;
    }

    public void setTimestampCarga(String timestampCarga) {
        this.timestampCarga = timestampCarga;
    }

    public String getTimestampAcceso() {
        return timestampAcceso;
    }

    public void updateTimestampAcceso() {
        this.timestampAcceso = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS"));
        ;
    }

    public boolean isFallo() {
        return fallo;
    }

    public void setFallo(boolean fallo) {
        this.fallo = fallo;
    }
}
