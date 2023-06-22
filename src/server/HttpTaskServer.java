package server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.FileBackedTasksManager;
import manager.Managers;
import manager.TaskManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import model.LocalDateTimeAdapter;
import model.Task;
import model.TaskStatus;
import model.TaskType;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpServer tasksServer;
    private final TaskManager tasksManager;
    private final Gson gson;
    private static final LocalDateTimeAdapter adapter = new LocalDateTimeAdapter();


    public HttpTaskServer() throws IOException {
        tasksServer = HttpServer.create(new InetSocketAddress(PORT),0);
        //tasksManager = Managers.getDefault();
        tasksManager = FileBackedTasksManager.loadFromFile("resources/lastSessionSaved.csv");
        gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, adapter.nullSafe())
                .setPrettyPrinting().create();
        tasksServer.createContext("/tasks/task", this::tasksOperations);
    }

    private void tasksOperations(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().toString().split("/");
        if("GET".equals(exchange.getRequestMethod())&exchange.getRequestURI().toString().endsWith("tasks/task")){
            String response = gson.toJson(tasksManager.returnAllTasks());
            sendJson(exchange,response,200);
        }
        else if ("GET".equals(exchange.getRequestMethod())&path[2].startsWith("task?id=")){
            int taskId = Integer.parseInt(path[path.length-1].substring(8));
            String response = gson.toJson(tasksManager.returnTaskByID(taskId),Task.class);
            sendJson(exchange,response,200);
        }
        else if ("POST".equals(exchange.getRequestMethod())&exchange.getRequestURI().toString().endsWith("tasks/task")){
            try{
                String request = readString(exchange);
                System.out.println(request);
                Task newTask = gson.fromJson(request,Task.class);
                System.out.println(newTask);
                tasksManager.addNewTask(newTask);
                System.out.println(tasksManager.returnAllTasks().toString());
                sendJson(exchange,"task added",200);
            }
            catch(JsonIOException e){
                sendString(exchange,"error json",404);
            }
        } else if ("DELETE".equals(exchange.getRequestMethod())&path[2].startsWith("task?id=")) {
            int taskId = Integer.parseInt(path[path.length-1].substring(8));
            boolean isDeleted = tasksManager.removeTaskByID(taskId);
            if (isDeleted) {
                sendString(exchange, "task deleted", 200);
            } else {
                sendString(exchange, "not found", 404);
            }
        } else if ("DELETE".equals(exchange.getRequestMethod())&exchange.getRequestURI().toString().endsWith("tasks/task")) {
            tasksManager.removeAllTasks();
            sendString(exchange, "tasks cleared", 200);
        } else{
            sendString(exchange, "unknown request",404);
        }

    }
    protected String readString(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendJson(HttpExchange exchange, String text,int code) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
    protected void sendString(HttpExchange exchange, String text,int code) throws IOException {
        byte[] resp = text.getBytes(UTF_8);;
        exchange.sendResponseHeaders(code, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        tasksServer.start();
    }

    public static void main(String[] args) throws IOException {
         new HttpTaskServer().start();
}}
