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
import java.util.HashMap;

public class Chatbot {

    JsonObject context;
    Service service;
    HashMap<String, Object> Entidades;
    Autor Autor;
    File File;

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
        service = new Service();
        this.Autor = new Autor();
        this.File = new File();
        this.Entidades = new HashMap<String, Object>();
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
        } else if (userUtterance.matches("menu|opciones|que puedes hacer")) {
            userAction.add("userIntent", new JsonPrimitive("intentopciones"));
        } else {
            String userType = " n ";
            if (userInput.has("userType")) {
                userType = userInput.get("userType").getAsString();
                userType = userType.replaceAll("%2C", ",");
            }

            if (userType != null) {
                String[] entrada = userType.split(":");
                if (entrada[0].trim().equals("requestsaludo")) {
                    userAction.add("userIntent", new JsonPrimitive("intentsaludo"));
                } else if (entrada[0].trim().equals("requestMenuOpciones")) {
                    userAction.add("userIntent", new JsonPrimitive("intentMenuOpciones"));
                } else if (entrada[0].trim().equals("requestEnviarImagen")) {
                    userAction.add("userIntent", new JsonPrimitive("PreguntaEnviarImagen"));
                } else if (entrada[0].trim().equals("requestConsultarImagen")) {
                    userAction.add("userIntent", new JsonPrimitive("PreguntaConsultarImagen"));
                } else {
                    userAction.add("userIntent", new JsonPrimitive("intentError"));
                }
                if (entrada.length > 1) {
                }
            }
            String userImagen = "";
            if (userInput.has("userImagen")) {
                userImagen = userInput.get("userImagen").getAsString();
                //userImage = userType.replaceAll("%2C", ",");
            }
            if (userImagen != null && userImagen.length() != 0) {
                if (userType != null) {
                    if (userType.equals("saveEnviarImagen")) {
                        this.File.setUrl(userImagen);
                        userAction.add("userIntent", new JsonPrimitive("responseEnviarImagen"));
                    } else if (userType.equals("saveConsultarImagen")) {
                        this.File.setUrl(userImagen);
                        userAction.add("userIntent", new JsonPrimitive("responseEnviarImagen"));
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

        if (userIntent.equals("intentError")) {
            context.add("currentTask", new JsonPrimitive("taskError"));
        } else if (userIntent.equals("intentsaludo")) {
            context.add("currentTask", new JsonPrimitive("tasksaludo"));
        } else if (userIntent.equals("intentopciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("intentsaludo")) {
            context.add("currentTask", new JsonPrimitive("tasksaludo"));
        } else if (userIntent.equals("intentMenuOpciones")) {
            context.add("currentTask", new JsonPrimitive("taskMenuOpciones"));
        } else if (userIntent.equals("PreguntaEnviarImagen")) {
            context.add("currentTask", new JsonPrimitive("taskEnviarImagen"));
        } else if (userIntent.equals("PreguntaConsultarImagen")) {
            context.add("currentTask", new JsonPrimitive("taskConsultarImagen"));
        } else if (userIntent.equals("responseEnviarImagen")) {
            context.add("currentTask", new JsonPrimitive("taskresponseEnviarImagen"));
        }
        
    }

    public void identifyBotIntent() {
        String currentTask = context.get("currentTask").getAsString();
        if (currentTask.equals("taskError")) {
            context.add("botIntent", new JsonPrimitive("botError"));
        } else if (currentTask.equals("tasksaludo")) {
            context.add("botIntent", new JsonPrimitive("botsaludo"));
        } else if (currentTask.equals("taskopciones")) {
            context.add("botIntent", new JsonPrimitive("botopciones"));
        } else if (currentTask.equals("tasksaludo")) {
            context.add("botIntent", new JsonPrimitive("botsaludo"));
        } else if (currentTask.equals("taskMenuOpciones")) {
            context.add("botIntent", new JsonPrimitive("botMenuOpciones"));
        } else if (currentTask.equals("taskEnviarImagen")) {
            context.add("botIntent", new JsonPrimitive("botEnviarImagen"));
        } else if (currentTask.equals("taskConsultarImagen")) {
            context.add("botIntent", new JsonPrimitive("botConsultarImagen"));
        } else if (currentTask.equals("taskresponseEnviarImagen")) {
            context.add("botIntent", new JsonPrimitive("botresponseEnviarImagen"));
        }
    }

    public JsonObject getBotOutput() throws IOException {

        JsonObject out = new JsonObject();
        String botIntent = context.get("botIntent").getAsString();
        JsonArray buttons = new JsonArray();
        String botUtterance = "";
        String type = "";

        if (botIntent.equals("botError")) {
            botUtterance = "no puedo ayudarle con su solicitud ";
            type = "Error";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botsaludo") || botIntent.equals("botsaludo")) {
            type = "saludo";
            botUtterance = "hola en que puedo ayudarte?";
            out = getbotsaludo();
        } else if (botIntent.equals("botopciones") || botIntent.equals("botMenuOpciones")) {
            type = "MenuOpciones";
            botUtterance = "opciones:";
            out = getbotMenuOpciones();
        } else if (botIntent.equals("botEnviarImagen")) {
            type = "EnviarImagen";
            botUtterance = "envie la imagen";
            out = getbotsaludo();
        } else if (botIntent.equals("botConsultarImagen")) {
            type = "ConsultarImagen";
            botUtterance = "envie la imagen";
            out = getbotsaludo();
        }else if (botIntent.equals("botresponseEnviarImagen")) {
            type = "responseEnviarImagen";
            botUtterance = "url imagen: "+this.File.getUrl();
            out = getbotsaludo();
        }
        out.add("botIntent", context.get("botIntent"));
        out.add("botUtterance", new JsonPrimitive(botUtterance));
        out.add("type", new JsonPrimitive(type));
        System.out.println("context: " + context.toString());
        System.out.println("salida: " + out.toString());
        return out;
    }

    public JsonObject getbotsaludo() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotMenuOpciones() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("enviar imagen"));
        b.add("respuesta", new JsonPrimitive("requestError"));
        buttons.add(b);
        b = new JsonObject();
        b.add("titulo", new JsonPrimitive("consultar info de imagen"));
        b.add("respuesta", new JsonPrimitive("requestError"));
        buttons.add(b);
        out.add("buttons", buttons);
        return out;
    }
}
