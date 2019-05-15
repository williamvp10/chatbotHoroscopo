package chatbot;

public class Horoscopo {

    private String date;
    private String horoscope;
    private String sunsign;

    public Horoscopo() {
    }

    public Horoscopo(String date, String horoscope, String sunsign) {
        this.date = date;
        this.horoscope = horoscope;
        this.sunsign = sunsign;
    }

    public String getDate() {
        return date;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public String getSunsign() {
        return sunsign;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public void setSunsign(String sunsign) {
        this.sunsign = sunsign;
    }
}
