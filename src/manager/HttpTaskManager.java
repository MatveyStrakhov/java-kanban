package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager{
    private final Gson gson;
    KVTaskClient client;
    public HttpTaskManager(String url) {
        this.client = new KVTaskClient(url);
        this.gson  = Managers.getDefaultGson();
        loadFromServer();
    }
    @Override
    protected void save() throws ManagerSaveException{
        try{

            for (Task task : tasks.values()) {
                String value = gson.toJson(task,Task.class);
                String key = String.valueOf(task.getTaskID());
                client.put(key,value);
            }

            for (Subtask task : subtasks.values()) {
                String value = gson.toJson(task,Subtask.class);
                String key = String.valueOf(task.getTaskID());
                client.put(key,value);
            }
            for (Epic task : epics.values()) {
                String value = gson.toJson(task,Epic.class);
                String key = String.valueOf(task.getTaskID());
                client.put(key,value);
            }
            if(!historyToString(historyManager).isEmpty()){
            client.put("history", historyToString(historyManager));}
            if(!getPrioritizedTasks().isEmpty()) {
                client.put("prioritizedTasks", getPrioritizedTasksOrder());
            }
        } catch (InterruptedException|IOException e) {
            throw new ManagerSaveException("Error while saving");
        }
    }
    public void loadFromServer() {
        int i = 0;
        try {
            while (!client.get(String.valueOf(i)).isEmpty()) {
                String response = client.get(String.valueOf(i));
                if (response.contains("TASK") & !response.contains("SUB")) {
                    Task newTask = gson.fromJson(response, Task.class);
                    tasks.put(i, newTask);
                } else if (response.contains("EPIC")) {
                    Epic newTask = gson.fromJson(response, Epic.class);
                    epics.put(i, newTask);
                } else if (response.contains("SUBTASK")) {
                    Subtask newTask = gson.fromJson(response, Subtask.class);
                    subtasks.put(i, newTask);
                }
                i++;
            }
            if (!client.get("history").isEmpty()) {
                String[] history = client.get("history").split(",");
                List<Integer> taskIds = Arrays.stream(history).map(Integer::parseInt).collect(Collectors.toList());
                for (int taskID : taskIds) {
                    if (tasks.containsKey(taskID)) {
                        historyManager.historyAdd(tasks.get(taskID));
                    } else if (epics.containsKey(taskID)) {
                        historyManager.historyAdd(epics.get(taskID));
                    } else if (subtasks.containsKey(taskID)) {
                        historyManager.historyAdd(subtasks.get(taskID));
                    }
                }
            }
            if (!client.get("prioritizedTasks").isEmpty() & !(client.get("prioritizedTasks").contentEquals("[]"))) {
                String[] sorted = client.get("prioritizedTasks").split(",");
                List<Integer> taskIds = Arrays.stream(sorted).map(Integer::parseInt).collect(Collectors.toList());
                for (int taskID : taskIds) {
                    if (tasks.containsKey(taskID)) {
                        sortedTasks.add(tasks.get(taskID));
                    } else if (epics.containsKey(taskID)) {
                        sortedTasks.add(epics.get(taskID));
                    } else if (subtasks.containsKey(taskID)) {
                        sortedTasks.add(subtasks.get(taskID));
                    }
                }
            }
        }
        catch (IOException | InterruptedException e){
            System.out.println("Error occurred while loading from server");
        }

    }
}
