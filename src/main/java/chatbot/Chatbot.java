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

import java.util.logging.Level;
import java.util.logging.Logger;

public class Chatbot {

    JsonObject context;
    Service service;
    String userName;

    Horoscopo horoscopo;

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
        userName = "";
        this.horoscopo = new Horoscopo();
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
        //info usuario 
        if (userInput.has("userName")) {
            userName = userInput.get("userName").getAsString();;
        }
        System.out.println("user: " + userName);
        //default case
        userAction.add("userIntent", new JsonPrimitive(""));
        if (userInput.has("userUtterance")) {
            userUtterance = userInput.get("userUtterance").getAsString();
            userUtterance = userUtterance.replaceAll("%2C", ",");
        }

        if (userUtterance.matches("Hola|hola|tengo una pregunta|Buen dia |buen dia ")) {
            userAction.add("userIntent", new JsonPrimitive("intentsaludo"));
        } else if (userUtterance.matches("Gracias|gracias")) {
            userAction.add("userIntent", new JsonPrimitive("intentagradecimiento"));
        } else {
            String userType = " n ";
            if (userInput.has("userType")) {
                userType = userInput.get("userType").getAsString();
                userType = userType.replaceAll("%2C", ",");
            }

            if (userType != null) {
                String[] entrada = userType.split(":");
                if (entrada[0].trim().equals("requestPreguntaSigno")) {
                    userAction.add("userIntent", new JsonPrimitive("intentPreguntaSigno"));
                } else if (entrada[0].trim().equals("requestAgradecer")) {
                    userAction.add("userIntent", new JsonPrimitive("intentAgradecer"));
                } else if (entrada[0].trim().equals("requestServicioHoroscopo")) {
                    userAction.add("userIntent", new JsonPrimitive("intentServicioHoroscopo"));
                } else {
                    userAction.add("userIntent", new JsonPrimitive("intentdefault"));
                }
                if (entrada[0].trim().equals("requestServicioHoroscopo")) {
                    this.horoscopo.setSunsign(userUtterance);
                }

                if (entrada.length > 1) {
                }
            }
        }
        return userAction;
    }

    public void updateContext(JsonObject userAction) {
        //copy userIntent
        context.add("userIntent", userAction.get("userIntent"));
        String userIntent = context.get("userIntent").getAsString();

        if (userIntent.equals("intentdefault")) {
            context.add("currentTask", new JsonPrimitive("taskdefault"));
        } else if (userIntent.equals("intentsaludo")) {
            context.add("currentTask", new JsonPrimitive("taskPreguntaSigno"));
        } else if (userIntent.equals("intentagradecimiento")) {
            context.add("currentTask", new JsonPrimitive("taskAgradecer"));
        } else if (userIntent.equals("intentPreguntaSigno")) {
            context.add("currentTask", new JsonPrimitive("taskPreguntaSigno"));
        } else if (userIntent.equals("intentAgradecer")) {
            context.add("currentTask", new JsonPrimitive("taskAgradecer"));
        } else if (userIntent.equals("intentServicioHoroscopo")) {
            context.add("currentTask", new JsonPrimitive("taskServicioHoroscopo"));
        }
    }

    public void identifyBotIntent() {
        String currentTask = context.get("currentTask").getAsString();
        if (currentTask.equals("taskdefault")) {
            context.add("botIntent", new JsonPrimitive("botdefault"));
        } else if (currentTask.equals("tasksaludo")) {
            context.add("botIntent", new JsonPrimitive("botsaludo"));
        } else if (currentTask.equals("taskagradecimiento")) {
            context.add("botIntent", new JsonPrimitive("botagradecimiento"));
        } else if (currentTask.equals("taskPreguntaSigno")) {
            context.add("botIntent", new JsonPrimitive("botPreguntaSigno"));
        } else if (currentTask.equals("taskAgradecer")) {
            context.add("botIntent", new JsonPrimitive("botAgradecer"));
        } else if (currentTask.equals("taskServicioHoroscopo")) {
            context.add("botIntent", new JsonPrimitive("botServicioHoroscopo"));
        }
    }

    public JsonObject getBotOutput() throws IOException {

        JsonObject out = new JsonObject();
        String botIntent = context.get("botIntent").getAsString();
        JsonArray buttons = new JsonArray();
        String botUtterance = "";
        String type = "";

        if (botIntent.equals("botdefault")) {
            botUtterance = "lo siento, no puedo ayudarte en tu solicitud ";
            type = "default";
            JsonObject b = null;
            out.add("buttons", buttons);
        } else if (botIntent.equals("botsaludo") || botIntent.equals("botPreguntaSigno")) {
            type = "PreguntaSigno";
            botUtterance = "Hola Digita tu Signo";
            out = getbotPreguntaSigno();
        } else if (botIntent.equals("botagradecimiento") || botIntent.equals("botAgradecer")) {
            type = "Agradecer";
            botUtterance = "Un placer";
            out = getbotAgradecer();
        } else if (botIntent.equals("botServicioHoroscopo")) {
            type = "ServicioHoroscopo";
            botUtterance = "your Horoscope";
            out = getbotServicioHoroscopo();
        }
        out.add("botIntent", context.get("botIntent"));
        out.add("botUtterance", new JsonPrimitive(botUtterance));
        out.add("type", new JsonPrimitive(type));
        System.out.println("context: " + context.toString());
        System.out.println("salida: " + out.toString());
        return out;
    }

    public JsonObject getbotPreguntaSigno() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotAgradecer() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonArray elements = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = new JsonArray();
        JsonObject e = new JsonObject();
        out.add("buttons", buttons);
        return out;
    }

    public JsonObject getbotServicioHoroscopo() {
        JsonObject out = new JsonObject();
        JsonArray buttons = new JsonArray();
        JsonObject b = null;
        JsonArray b1 = null;
        JsonArray elements = new JsonArray();
        JsonObject e = null;
        JsonObject servicio = null;
        try {
            servicio = service.getServicioHoroscopo(this.horoscopo);
        } catch (IOException ex) {
            Logger.getLogger(Chatbot.class.getName()).log(Level.SEVERE, null, ex);
        }

        e = new JsonObject();
        JsonObject obj = (JsonObject) servicio.get("horoscopo").getAsJsonObject();
        e.add("titulo", new JsonPrimitive("" + "" + obj.get("hroscope").getAsString()));
        e.add("subtitulo", new JsonPrimitive("" + "" + obj.get("hroscope").getAsString()));
        e.add("url", new JsonPrimitive("" + "https://cdne.ojo.pe/thumbs/uploads/img/2018/05/22/cuales-son-las-fechas-de-los-decanatos-de-cada-sig-256596-jpg_700x0.jpg"));
        e.add("buttons", new JsonArray());
        elements.add(e);
        out.add("buttons", buttons);
        out.add("elements", elements);
        return out;
    }
}
