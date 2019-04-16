package chatbot;

import Services.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;

public class Chatbot1 {

    JsonObject context;
    Service service;

    Sensor sensor;
    Actuador actuador;
    Sensors sensores;

    public static void main(String[] args) throws IOException {
        Chatbot c = new Chatbot();
        Scanner scanner = new Scanner(System.in);
        String userUtterance;

        do {
            System.out.print("User:");
            userUtterance = scanner.nextLine();

            JsonObject userInput = new JsonObject();
            userInput.add("userUtterance", new JsonPrimitive(userUtterance));
            JsonObject botOutput = c.process(userInput);
            String botUtterance = "";
            if (botOutput != null && botOutput.has("botUtterance")) {
                botUtterance = botOutput.get("botUtterance").getAsString();
            }
            System.out.println("Bot:" + botUtterance);

        } while (!userUtterance.equals("QUIT"));
        scanner.close();
    }

    public Chatbot1() {
        context = new JsonObject();
        context.add("currentTask", new JsonPrimitive("none"));
        service = new Service();
        this.sensor = new Sensor();
        this.actuador = new Actuador();
        this.sensores = new Sensors();
    }

    public JsonObject process(JsonObject userInput) throws IOException {
        System.out.println(userInput.toString());
        //step1: process user input
        JsonObject userAction = processUserInput(userInput);

        //step2: update context
        updateContext(userAction);

        //step3: identify bot intent
        identifyBotIntent();
        System.out.println("context " + context.toString());
        //step4: structure output
        JsonObject out = getBotOutput();
        System.out.println("out " + out.toString());
        return out;
    }

    public String processFB(JsonObject userInput) throws IOException {
        JsonObject out = process(userInput);
        return out.toString();
    }

    public JsonObject processUserInput(JsonObject userInput) throws IOException {
        String userUtterance = null;
        JsonObject userAction = new JsonObject();
        //default case
        userAction.add("userIntent", new JsonPrimitive(""));
        if (userInput.has("userUtterance")) {
            userUtterance = userInput.get("userUtterance").getAsString();
            userUtterance = userUtterance.replaceAll("%2C", ",");
        }

        if (userUtterance.matches("hola|buenos dias|buenas tardes")) {
            userAction.add("userIntent", new JsonPrimitive("intentsaludo"));
        } else if (userUtterance.matches("opciones|menu")) {
            userAction.add("userIntent", new JsonPrimitive("intentopciones"));
        } else if (userUtterance.matches("gracias")) {
            userAction.add("userIntent", new JsonPrimitive("intentagradecimiento"));
        } else {
            String userType = " n ";
            if (userInput.has("userType")) {
                userType = userInput.get("userType").getAsString();
                userType = userType.replaceAll("%2C", ",");
            }

            if (userType != null) {
                String[] entrada = userType.split(":");
                if (entrada[0].trim().equals("requestSaludo")) {
                    userAction.add("userIntent", new JsonPrimitive("intentSaludo"));
                } else if (entrada[0].trim().equals("requestMenuOpciones")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuOpciones"));
                } else if (entrada[0].trim().equals("requestAllSensors")) {
                    userAction.add("userIntent", new JsonPrimitive("intentAllSensors"));
                } else if (entrada[0].trim().equals("requestIdSensor")) {
                    userAction.add("userIntent", new JsonPrimitive("intentIdSensor"));
                } else if (entrada[0].trim().equals("requestInfoSensor")) {
                    userAction.add("userIntent", new JsonPrimitive("intentInfoSensor"));
                } else if (entrada[0].trim().equals("requestEstadoActuador")) {
                    userAction.add("userIntent", new JsonPrimitive("intentEstadoActuador"));
                } else if (entrada[0].trim().equals("requestModificarActuador")) {
                    userAction.add("userIntent", new JsonPrimitive("intentModificarActuador"));
                } else if (entrada[0].trim().equals("requestagradecimiento")) {
                    userAction.add("userIntent", new JsonPrimitive("intentagradecimiento"));
                } else {
                    userAction.add("userIntent", new JsonPrimitive("intenterror"));
                }
                if (entrada.length > 1) {
                    if (entrada[1].equals("AllSensors")) {
                        context.add("AllSensors", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cAllSensors = new AllSensors();
                            String[] data = entrada[2].split("--");
                        }
                    }
                    if (entrada[1].equals("IdSensor")) {
                        context.add("IdSensor", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cIdSensor = new IdSensor();
                            String[] data = entrada[2].split("--");
                        }
                    }
                    if (entrada[1].equals("InfoSensor")) {
                        context.add("InfoSensor", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cInfoSensor = new InfoSensor();
                            String[] data = entrada[2].split("--");
                            this.cInfoSensor.setid(data[0].split("-")[1]);
                            this.cInfoSensor.setestado(data[1].split("-")[1]);
                            this.cInfoSensor.settemperatura(data[2].split("-")[1]);
                            this.cInfoSensor.seturl(data[3].split("-")[1]);
                        }
                    }
                    if (entrada[1].equals("EstadoActuador")) {
                        context.add("EstadoActuador", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cEstadoActuador = new EstadoActuador();
                            String[] data = entrada[2].split("--");
                            this.cEstadoActuador.setid(data[0].split("-")[1]);
                            this.cEstadoActuador.setestado(data[1].split("-")[1]);
                            this.cEstadoActuador.setvalor(data[2].split("-")[1]);
                            this.cEstadoActuador.seturl(data[3].split("-")[1]);
                        }
                    }
                    if (entrada[1].equals("ModificarActuador")) {
                        context.add("ModificarActuador", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cModificarActuador = new ModificarActuador();
                            String[] data = entrada[2].split("--");
                            this.cModificarActuador.setid(data[0].split("-")[1]);
                            this.cModificarActuador.setestado(data[1].split("-")[1]);
                            this.cModificarActuador.setvalor(data[2].split("-")[1]);
                            this.cModificarActuador.seturl(data[3].split("-")[1]);
                        }
                    }
                }
            }
        }
        return userAction;
    }

    public void updateContext(JsonObject userAction) {
        //copy userIntent
        context.add("userIntent", userAction.get("userIntent"));
        String userIntent = context.get("userIntent").getAsString();

        if (userIntent.equals("intenterror")) {
            context.add("currentTask", new JsonPrimitive("taskerror"));
        } else if (userIntent.equals("intentsaludo")) {
            context.add("currentTask", new JsonPrimitive("taskSaludo"));
        } else if (userIntent.equals("intentopciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("intentagradecimiento")) {
            context.add("currentTask", new JsonPrimitive("taskagradecimiento"));
        } else if (userIntent.equals("intentSaludo")) {
            context.add("currentTask", new JsonPrimitive("taskSaludo"));
        } else if (userIntent.equals("intentMenuOpciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("intentAllSensors")) {
            context.add("currentTask", new JsonPrimitive("taskAllSensors"));
        } else if (userIntent.equals("intentIdSensor")) {
            context.add("currentTask", new JsonPrimitive("taskIdSensor"));
        } else if (userIntent.equals("intentInfoSensor")) {
            context.add("currentTask", new JsonPrimitive("taskInfoSensor"));
        } else if (userIntent.equals("intentEstadoActuador")) {
            context.add("currentTask", new JsonPrimitive("taskEstadoActuador"));
        } else if (userIntent.equals("intentModificarActuador")) {
            context.add("currentTask", new JsonPrimitive("taskModificarActuador"));
        } else if (userIntent.equals("intentagradecimiento")) {
            context.add("currentTask", new JsonPrimitive("taskagradecimiento"));
        }
    }

    public void identifyBotIntent() {
        String currentTask = context.get("currentTask").getAsString();
        if (currentTask.equals("taskerror")) {
            context.add("botIntent", new JsonPrimitive("boterror"));
        } else if (currentTask.equals("tasksaludo")) {
            context.add("botIntent", new JsonPrimitive("botsaludo"));
        } else if (currentTask.equals("taskopciones")) {
            context.add("botIntent", new JsonPrimitive("botopciones"));
        } else if (currentTask.equals("taskagradecimiento")) {
            context.add("botIntent", new JsonPrimitive("botagradecimiento"));
        } else if (currentTask.equals("taskSaludo")) {
            context.add("botIntent", new JsonPrimitive("botSaludo"));
        } else if (currentTask.equals("taskMenuOpciones")) {
            context.add("botIntent", new JsonPrimitive("botMenuOpciones"));
        } else if (currentTask.equals("taskAllSensors")) {
            context.add("botIntent", new JsonPrimitive("botAllSensors"));
        } else if (currentTask.equals("taskIdSensor")) {
            context.add("botIntent", new JsonPrimitive("botIdSensor"));
        } else if (currentTask.equals("taskInfoSensor")) {
            context.add("botIntent", new JsonPrimitive("botInfoSensor"));
        } else if (currentTask.equals("taskEstadoActuador")) {
            context.add("botIntent", new JsonPrimitive("botEstadoActuador"));
        } else if (currentTask.equals("taskModificarActuador")) {
            context.add("botIntent", new JsonPrimitive("botModificarActuador"));
        } else if (currentTask.equals("taskagradecimiento")) {
            context.add("botIntent", new JsonPrimitive("botagradecimiento"));
        }
    }

    public JsonObject getBotOutput() throws IOException {

        JsonObject out = new JsonObject();
        String botIntent = context.get("botIntent").getAsString();
        JsonArray buttons = new JsonArray();
        String botUtterance = "";
        String type = "";

        if (botIntent.equals("boterror")) {
            botUtterance = "no puedo ayudarle con su solicitud ";
            type = "error";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botsaludo")) {
            botUtterance = "hola en que puedo ayudarte? ";
            type = "Saludo";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botopciones")) {
            botUtterance = "opciones: ";
            type = "MenuOpciones";
            JsonObject b = null;
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado sensores"));
            b.add("respuesta", new JsonPrimitive("requestAllSensors"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado unico sensor"));
            b.add("respuesta", new JsonPrimitive("requestIdSensor"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado actuador"));
            b.add("respuesta", new JsonPrimitive("requestEstadoActuador"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("modificar actuador"));
            b.add("respuesta", new JsonPrimitive("requestModificarActuador"));
            buttons.add(b);
            out.add("buttons", buttons);
        } else if (botIntent.equals("botagradecimiento")) {
            botUtterance = "con gusto :) ";
            type = "agradecimiento";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botSaludo")) {
            type = "Saludo";
            botUtterance = "hola en que puedo ayudarte?";
            JsonObject b = null;
        } else if (botIntent.equals("botMenuOpciones")) {
            type = "MenuOpciones";
            botUtterance = "opciones:";
            JsonObject b = null;
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado sensores"));
            b.add("respuesta", new JsonPrimitive("requestAllSensors"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado unico sensor"));
            b.add("respuesta", new JsonPrimitive("requestIdSensor"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado actuador"));
            b.add("respuesta", new JsonPrimitive("requestEstadoActuador"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("modificar actuador"));
            b.add("respuesta", new JsonPrimitive("requestModificarActuador"));
            buttons.add(b);
        } else if (botIntent.equals("botAllSensors")) {
            type = "AllSensors";
            botUtterance = "todos los sensores";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getAllSensors();
            JsonArray elementosServicio = (JsonArray) servicio.get("sensores").getAsJsonArray();
            null  out.add("elements", elements);
        } else if (botIntent.equals("botIdSensor")) {
            type = "IdSensor";
            botUtterance = "escoja sensor";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getIdSensor();
            JsonArray elementosServicio = (JsonArray) servicio.get("sensores").getAsJsonArray();
            null  out.add("elements", elements);
        } else if (botIntent.equals("botInfoSensor")) {
            type = "InfoSensor";
            botUtterance = "info sensores";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getInfoSensor(, , ,);
            JsonArray elementosServicio = (JsonArray) servicio.get("sensor").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "id: " + obj.get("id").getAsString() + "  estado: " + obj.get("estado").getAsString() + " temperatura: " + obj.get("temperatura").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "id: " + obj.get("id").getAsString() + "  estado: " + obj.get("estado").getAsString() + " temperatura: " + obj.get("temperatura").getAsString()));
                e.add("url", ew JsonPrimitive("" + "" + obj.get("url").getAsString())
                );
  e.add("buttons", new JsonArray());
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botEstadoActuador")) {
            type = "EstadoActuador";
            botUtterance = "estado actuador";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getEstadoActuador();
            JsonArray elementosServicio = (JsonArray) servicio.get("actuador").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "valor: " + obj.get("valor").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "valor: " + obj.get("valor").getAsString()));
                e.add("url", ew JsonPrimitive("" + "" + obj.get("url").getAsString())
                );
  e.add("buttons", new JsonArray());
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botModificarActuador")) {
            type = "ModificarActuador";
            botUtterance = "actuador modificado";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getModificarActuador();
            JsonArray elementosServicio = (JsonArray) servicio.get("actuador").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                JsonObject obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "valor: " + obj.get("valor").getAsString()));
                e.add("subtitulo", new JsonPrimitive("" + "valor: " + obj.get("valor").getAsString()));
                e.add("url", ew JsonPrimitive("" + "" + obj.get("url").getAsString())
                );
  e.add("buttons", new JsonArray());
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botagradecimiento")) {
            type = "agradecimiento";
            botUtterance = "con gusto :)";
            JsonObject b = null;
        }
        out.add("buttons", buttons);
        out.add("botIntent", context.get("botIntent"));
        out.add("botUtterance", new JsonPrimitive(botUtterance));
        out.add("type", new JsonPrimitive(type));
        System.out.println("context: " + context.toString());
        System.out.println("salida: " + out.toString());
        return out;
    }
}
