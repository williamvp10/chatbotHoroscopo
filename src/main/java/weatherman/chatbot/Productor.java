package weatherman.chatbot;

public class Productor {

    private String tipo;
    private String precio;

    public Productor() {
    }

    public Productor(String tipo, String precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    public String gettipo() {
        return tipo;
    }

    public String getprecio() {
        return precio;
    }

    public void settipo(String tipo) {
        this.tipo = tipo;
    }

    public void setprecio(String precio) {
        this.precio = precio;
    }
}
