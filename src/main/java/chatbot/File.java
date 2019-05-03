package chatbot;

public class File {

    private String url;
    private String hash;
    private String addressAutor;

    public File() {
        this.url = "";
        this.hash = "";
        this.addressAutor = "";
    }

    public File(String url, String hash, String addressAutor) {
        this.url = url;
        this.hash = hash;
        this.addressAutor = addressAutor;
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    public String getAddressAutor() {
        return addressAutor;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setAddressAutor(String addressAutor) {
        this.addressAutor = addressAutor;
    }
}
