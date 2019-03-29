package weatherman.chatbot;

import Services.Service1;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParser;
import java.util.ArrayList;

public class Chatbot {

    JsonObject context;
    Service1 service;

    AllSensors cAllSensors;
    IDSensor cIDSensor;
    InfoSensor cInfoSensor;

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

    public Chatbot() {
        context = new JsonObject();
        context.add("currentTask", new JsonPrimitive("none"));
        service = new Service1();
        this.cAllSensors = new AllSensors();
        this.cIDSensor = new IDSensor();
        this.cInfoSensor = new InfoSensor();
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

        if (userUtterance.matches("hola")) {
            userAction.add("userIntent", new JsonPrimitive("intenthola"));
        } else if (userUtterance.matches("opciones")) {
            userAction.add("userIntent", new JsonPrimitive("intentopciones"));
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
                } else if (entrada[0].trim().equals("requestIDSensor")) {
                    userAction.add("userIntent", new JsonPrimitive("intentIDSensor"));
                } else if (entrada[0].trim().equals("requestInfoSensor")) {
                    userAction.add("userIntent", new JsonPrimitive("intentInfoSensor"));
                } else if (entrada[0].trim().equals("requestayuda")) {
                    userAction.add("userIntent", new JsonPrimitive("intentayuda"));
                } else {
                    userAction.add("userIntent", new JsonPrimitive("intenterror"));
                }
                if (entrada.length > 1) {
                    if (entrada[1].equals("AllSensors")) {
                        context.add("AllSensors", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cAllSensors = new AllSensors();
                            String[] data = entrada[2].split("--");
                            this.cAllSensors.setid(data[0].split("-")[1]);
                            this.cAllSensors.setestado(data[1].split("-")[1]);
                            this.cAllSensors.settemperatura(data[2].split("-")[1]);
                        }
                    }
                    if (entrada[1].equals("IDSensor")) {
                        context.add("IDSensor", new JsonPrimitive(userUtterance));
                        if (entrada.length > 2) {
                            this.cIDSensor = new IDSensor();
                            String[] data = entrada[2].split("--");
                            this.cIDSensor.setid(data[0].split("-")[1]);
                            this.cIDSensor.setestado(data[1].split("-")[1]);
                            this.cIDSensor.settemperatura(data[2].split("-")[1]);
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
        } else if (userIntent.equals("intenthola")) {
            context.add("currentTask", new JsonPrimitive("taskSaludo"));
        } else if (userIntent.equals("intentopciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("intentSaludo")) {
            context.add("currentTask", new JsonPrimitive("taskSaludo"));
        } else if (userIntent.equals("intentMenuOpciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("intentAllSensors")) {
            context.add("currentTask", new JsonPrimitive("taskAllSensors"));
        } else if (userIntent.equals("intentIDSensor")) {
            context.add("currentTask", new JsonPrimitive("taskIDSensor"));
        } else if (userIntent.equals("intentInfoSensor")) {
            context.add("currentTask", new JsonPrimitive("taskInfoSensor"));
        } else if (userIntent.equals("intentayuda")) {
            context.add("currentTask", new JsonPrimitive("taskayuda"));
        }
    }

    public void identifyBotIntent() {
        String currentTask = context.get("currentTask").getAsString();
        if (currentTask.equals("taskerror")) {
            context.add("botIntent", new JsonPrimitive("boterror"));
        } else if (currentTask.equals("taskhola")) {
            context.add("botIntent", new JsonPrimitive("bothola"));
        } else if (currentTask.equals("taskopciones")) {
            context.add("botIntent", new JsonPrimitive("botopciones"));
        } else if (currentTask.equals("taskSaludo")) {
            context.add("botIntent", new JsonPrimitive("botSaludo"));
        } else if (currentTask.equals("taskMenuOpciones")) {
            context.add("botIntent", new JsonPrimitive("botMenuOpciones"));
        } else if (currentTask.equals("taskAllSensors")) {
            context.add("botIntent", new JsonPrimitive("botAllSensors"));
        } else if (currentTask.equals("taskIDSensor")) {
            context.add("botIntent", new JsonPrimitive("botIDSensor"));
        } else if (currentTask.equals("taskInfoSensor")) {
            context.add("botIntent", new JsonPrimitive("botInfoSensor"));
        } else if (currentTask.equals("taskayuda")) {
            context.add("botIntent", new JsonPrimitive("botayuda"));
        }
    }

    public JsonObject getBotOutput() throws IOException {

        JsonObject out = new JsonObject();
        String botIntent = context.get("botIntent").getAsString();
        JsonArray buttons = new JsonArray();
        String botUtterance = "";
        String type = "";

        if (botIntent.equals("boterror")) {
            botUtterance = "lo siento no pude entender :( ";
            type = "error";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("bothola")) {
            botUtterance = "hola! en que puedo ayudarte? ";
            type = "Saludo";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botopciones")) {
            botUtterance = "sus opciones ";
            type = "MenuOpciones";
            JsonObject b = null;
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado de los sensores"));
            b.add("respuesta", new JsonPrimitive("requestAllSensors"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado de un sensor"));
            b.add("respuesta", new JsonPrimitive("requestIDSensor"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("ayuda"));
            b.add("respuesta", new JsonPrimitive("requestayuda"));
            buttons.add(b);
            out.add("buttons", buttons);
        } else if (botIntent.equals("botSaludo")) {
            type = "Saludo";
            botUtterance = "hola! en que puedo ayudarte?";
            JsonObject b = null;
        } else if (botIntent.equals("botMenuOpciones")) {
            type = "MenuOpciones";
            botUtterance = "sus opciones";
            JsonObject b = null;
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado de los sensores"));
            b.add("respuesta", new JsonPrimitive("requestAllSensors"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("estado de un sensor"));
            b.add("respuesta", new JsonPrimitive("requestIDSensor"));
            buttons.add(b);
            b = new JsonObject();
            b.add("titulo", new JsonPrimitive("ayuda"));
            b.add("respuesta", new JsonPrimitive("requestayuda"));
            buttons.add(b);
        } else if (botIntent.equals("botAllSensors")) {
            type = "AllSensors";
            botUtterance = "Sensores";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getAllSensors();
            JsonArray elementosServicio = (JsonArray) servicio.get("sensores").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "id: " + obj.get("id").getAsString() + " \n estado:" + obj.get("estado").getAsString() + "\n temperatura" + obj.get("temperatura").getAsString()));
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botIDSensor")) {
            type = "IDSensor";
            botUtterance = "escoja sensor";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getIDSensor();
            JsonArray elementosServicio = (JsonArray) servicio.get("sensores").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "id: " + obj.get("id").getAsString()));
                b = new JsonObject();
                b1 = new JsonArray();
                b.add("titulo", new JsonPrimitive(obj.get("Seleccionar").getAsString()));
                String var = "" + "id-" + obj.get("id").getAsString() + "--" + "estado-" + obj.get("estado").getAsString() + "--" + "temperatura-" + obj.get("temperatura").getAsString();
                b.add("respuesta", new JsonPrimitive("requestInfoSensor:IDSensor:" + var));
                b1.add(b);
                e.add("buttons", b1);
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botInfoSensor")) {
            type = "InfoSensor";
            botUtterance = "";
            JsonObject b = null;
            JsonArray b1 = null;
            JsonArray elements = new JsonArray();
            JsonObject e = null;
            JsonObject obj = null;
            JsonObject servicio = service.getInfoSensor(cIDSensor.getid());
            JsonArray elementosServicio = (JsonArray) servicio.get("sensores").getAsJsonArray();

            for (int i = 0; i < elementosServicio.size(); i++) {
                e = new JsonObject();
                obj = elementosServicio.get(i).getAsJsonObject();
                e.add("titulo", new JsonPrimitive("" + "id: " + obj.get("id").getAsString() + " \n estado:" + obj.get("estado").getAsString() + "\n temperatura" + obj.get("temperatura").getAsString()));
                elements.add(e);
            }
            out.add("elements", elements);
        } else if (botIntent.equals("botayuda")) {
            type = "ayuda";
            botUtterance = "bye ";
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
