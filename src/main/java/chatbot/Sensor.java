package chatbot;

import com.google.gson.annotations.SerializedName;

public class Sensor {
    @SerializedName("id")    
    private String id;
    @SerializedName("temperatura")
    private String temperatura;
    @SerializedName("humedad")
    private String humedad;
    @SerializedName("presion")
    private String presion;
    @SerializedName("fecha")
    private String fecha;
    @SerializedName("ejex")
    private String ejex;
    @SerializedName("ejey")
    private String ejey;
    @SerializedName("ejez")
    private String ejez;

    public Sensor() {
    }

    public Sensor(String id, String temperatura, String humedad, String presion, String fecha, String ejex, String ejey, String ejez) {
        this.id = id;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.presion = presion;
        this.fecha = fecha;
        this.ejex = ejex;
        this.ejey = ejey;
        this.ejez = ejez;
    }

    public String getId() {
        return id;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public String getHumedad() {
        return humedad;
    }

    public String getPresion() {
        return presion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEjex() {
        return ejex;
    }

    public String getEjey() {
        return ejey;
    }

    public String getEjez() {
        return ejez;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setEjex(String ejex) {
        this.ejex = ejex;
    }

    public void setEjey(String ejey) {
        this.ejey = ejey;
    }

    public void setEjez(String ejez) {
        this.ejez = ejez;
    }
}
