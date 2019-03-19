package weatherman.chatbot;

public class Ingredientes {

    private String ingredientes;
    private String precio;

    public Ingredientes() {
    }

    public Ingredientes(String ingredientes, String precio) {
        this.ingredientes = ingredientes;
        this.precio = precio;
    }

    public String getingredientes() {
        return ingredientes;
    }

    public String getprecio() {
        return precio;
    }

    public void setingredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void setprecio(String precio) {
        this.precio = precio;
    }
}
