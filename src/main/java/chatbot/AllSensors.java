 package chatbot;
public class AllSensors {
private String id;
private String estado;
private String url;
private String temperatura;
public AllSensors() {
   }
   
		public AllSensors(String id,String estado,String url,String temperatura){this.id = id;
this.estado = estado;
this.url = url;
this.temperatura = temperatura;
}public String getid() {
        return id;
    }
public String getestado() {
        return estado;
    }
public String geturl() {
        return url;
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
public void seturl(String url) {
       this.url = url;
    }
public void settemperatura(String temperatura) {
       this.temperatura = temperatura;
    }
}	
