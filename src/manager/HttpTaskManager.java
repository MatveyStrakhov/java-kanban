package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.ManagerSaveException;
import model.Epic;
import model.LocalDateTimeAdapter;
import model.Subtask;
import model.Task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager{
    private final Gson gson;
    KVTaskClient client;
    private static final LocalDateTimeAdapter adapter = new LocalDateTimeAdapter();
    public HttpTaskManager(String url) throws IOException, InterruptedException {
        this.client = new KVTaskClient(url);
        this.gson = new GsonBuilder().serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, adapter.nullSafe())
                .setPrettyPrinting().create();
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
    public void loadFromServer() throws IOException, InterruptedException {
        int i = 0;
        while(!client.get(String.valueOf(i)).isEmpty()){
            String response = client.get(String.valueOf(i));
            if(response.contains("TASK")){
                Task newTask = gson.fromJson(response, Task.class);
                tasks.put(i,newTask);
            }
            else if(response.contains("EPIC")){
                Epic newTask = gson.fromJson(response, Epic.class);
                epics.put(i,newTask);
            }
            else if(response.contains("SUBTASK")){
                Subtask newTask = gson.fromJson(response, Subtask.class);
                subtasks.put(i,newTask);
            }
            i++;
        }
        if(!client.get("history").isEmpty()){
            String[] history = client.get("history").split(",");
            List<Integer> taskIds = Arrays.stream(history).map(Integer::parseInt).collect(Collectors.toList());
            for(int taskID:taskIds){
                if(tasks.containsKey(taskID)){
                    historyManager.historyAdd(tasks.get(taskID));
                }
                else if(epics.containsKey(taskID)){
                    historyManager.historyAdd(epics.get(taskID));
                }
                else if(subtasks.containsKey(taskID)){
                    historyManager.historyAdd(subtasks.get(taskID));
                }
            }
        }
        if(!client.get("prioritizedTasks").isEmpty()&!(client.get("prioritizedTasks").contentEquals("[]"))){
            String[] sorted = client.get("prioritizedTasks").split(",");
            List<Integer> taskIds = Arrays.stream(sorted).map(Integer::parseInt).collect(Collectors.toList());
            for(int taskID:taskIds){
                if(tasks.containsKey(taskID)){
                    sortedTasks.add(tasks.get(taskID));
                }
                else if(epics.containsKey(taskID)){
                    sortedTasks.add(epics.get(taskID));
                }
                else if(subtasks.containsKey(taskID)){
                    sortedTasks.add(subtasks.get(taskID));
                }
            }
        }

    }

    @Override
    public void addNewSubtask(Subtask subtask, int epicId) {
        super.addNewSubtask(subtask, epicId);
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic task) {
        super.addNewEpic(task);
        save();
    }

    @Override
    public boolean removeTaskByID(int id) {
        if(super.removeTaskByID(id)){
            save();
            return true;
        }
        else {return false;}


    }

    @Override
    public boolean removeSubtaskByID(int id) {
        if(super.removeSubtaskByID(id)){
            save();
            return true;}
        else{return false;}

    }

    @Override
    public boolean removeEpicByID(int id) {
        if(super.removeEpicByID(id)){
            save();
            return true;}
        else{return false;}
    }

    @Override
    public void printTaskByID(int id) {
        super.printTaskByID(id);
        save();
    }

    @Override
    public void printSubtaskByID(int id) {
        super.printSubtaskByID(id);
        save();
    }

    @Override
    public void printEpicByID(int id) {
        super.printEpicByID(id);
        save();
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void updateEpic(Epic task, int id) {
        super.updateEpic(task, id);
        save();
    }

    @Override
    public void updateSubtask(Subtask task, int id) {
        super.updateSubtask(task, id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

}
