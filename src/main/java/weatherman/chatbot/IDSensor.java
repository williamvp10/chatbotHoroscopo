package weatherman.chatbot;

public class IDSensor {

    private String id;
    private String estado;
    private String temperatura;

    public IDSensor() {
    }

    public IDSensor(String id, String estado, String temperatura) {
        this.id = id;
        this.estado = estado;
        this.temperatura = temperatura;
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

    public void setid(String id) {
        this.id = id;
    }

    public void setestado(String estado) {
        this.estado = estado;
    }

    public void settemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
}
