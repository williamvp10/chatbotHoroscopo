package chatbot;

import java.util.ArrayList;

public class Sensors {

    private ArrayList<Sensor> sensor;

    public Sensors() {
        this.sensor= new ArrayList<>();
    }

    public Sensors(ArrayList sensor) {
        this.sensor=sensor;
    }

    public ArrayList<Sensor> getSensor() {
        return sensor;
    }

    public void setSensor(ArrayList<Sensor> sensor) {
        this.sensor = sensor;
    }
    
}
