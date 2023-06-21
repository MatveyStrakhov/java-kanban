package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpsServer tasksServer;
    private final TaskManager tasksManager;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        tasksServer = HttpsServer.create(new InetSocketAddress(PORT),0);
        tasksManager = Managers.getDefault();
        tasksServer.createContext("/tasks/task/", this::tasksOperations);
        gson = new Gson();
    }
    private void tasksOperations(HttpExchange exchange) throws IOException {
        if("GET".equals(exchange.getRequestMethod())&exchange.getRequestURI().toString().equals("/tasks/task/")){
            exchange.sendResponseHeaders(200,0);
            String response = gson.toJson(tasksManager.returnAllTasks());
            sendString(exchange,response);
        }

    }
    protected String readString(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendString(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
    }


}
