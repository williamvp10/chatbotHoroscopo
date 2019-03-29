 package chatbot;
public class ModificarActuador {
private String id;
private String estado;
private String valor;
private String url;
public ModificarActuador() {
   }
   
		public ModificarActuador(String id,String estado,String valor,String url){this.id = id;
this.estado = estado;
this.valor = valor;
this.url = url;
}public String getid() {
        return id;
    }
public String getestado() {
        return estado;
    }
public String getvalor() {
        return valor;
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
public void setvalor(String valor) {
       this.valor = valor;
    }
public void seturl(String url) {
       this.url = url;
    }
}	
