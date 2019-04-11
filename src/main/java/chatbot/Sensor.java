package chatbot;

public class Sensor {

    private String id;
    private String estado;
    private String temperatura;
    private String url;

    public Sensor() {
    }

    public Sensor(String id, String estado, String temperatura, String url) {
        this.id = id;
        this.estado = estado;
        this.temperatura = temperatura;
        this.url = url;
    }

    public String getid() {
        return id;
    }

    public String getestado() {
        return estado;
    }

    public String gettemperatura() {
        return temperatura;
    }

    public String geturl() {
        return url;
    }

    public void setid(String id) {
        this.id = id;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }

    public void settemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public void seturl(String url) {
        this.url = url;
    }
}
