import java.util.ArrayList;
import java.util.HashMap;
public class TaskManager {
    int rawID =0;
    HashMap<Integer,Task> tasks = new HashMap<>();
    HashMap<Integer,Epic> epics = new HashMap<>();
    HashMap<Integer,Subtask> subtasks = new HashMap<>();

    void addNewTask (Task task){
        task.setTaskID( generateID());
        tasks.put(task.getTaskID(),task);
    }
    void addNewSubtask (Subtask subtask, int epicID){
        if (epics.containsKey(epicID)){
            subtask.setTaskID(generateID());
            subtask.setTaskID(subtask.getTaskID());
            subtasks.put(subtask.getTaskID(),subtask);
            epics.get(epicID).mySubtasksID.add(subtask.getTaskID());
            checkStatusOfEpic(epics.get(epicID));
        }
    }
    void addNewEpic (Epic epic){
        epic.setTaskID(generateID());
        epics.put(epic.getTaskID(),epic);
    }

    private void checkStatusOfEpic(Epic epic) {
        boolean newTask = false;
        boolean inProgress = false;
        boolean done = false;
        if (!epics.isEmpty()) {
            for (Integer subtaskID : epic.mySubtasksID) {
                if (subtasks.get(subtaskID).getStatus().equals("IN_PROGRESS")) {
                    inProgress = true;
                } else if (subtasks.get(subtaskID).getStatus().equals("DONE")) {
                    done = true;
                } else if (subtasks.get(subtaskID).getStatus().equals("NEW")) {
                    newTask = true;
                }
            }
            if (inProgress) {
                epic.setStatus("IN_PROGRESS");
            } else if (!inProgress && done&&!newTask) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("NEW");
            }
        }
        else {
            epic.setStatus("NEW");
        }
    }
    void printAllTasks(){
        for (Task task:tasks.values()) {
            System.out.println(task.toString());
        }
    }
    void printAllEpics(){
        for (Epic epic:epics.values()) {
            System.out.println(epic.toString());
        }
    }
    void printAllSubtasks(){
        for (Subtask subtask:subtasks.values()) {
            System.out.println(subtask.toString());
        }
    }
    void printSubtasksByEpicID(int epicID){
        for (Integer subtaskID:epics.get(epicID).mySubtasksID) {
            System.out.println(subtasks.get(subtaskID).toString());
        }
    }
    void removeTaskByID(int taskID){
        if (tasks.containsKey(taskID)){
            System.out.println("1");
            tasks.remove(taskID);
            System.out.println("1");
        }
    }
    void removeEpicByID(int epicID){
        if (epics.containsKey(epicID)){
            epics.remove(epicID);
            for (Subtask subtask: subtasks.values()) {
                if (subtask.getEpicID()==epicID){
                    subtasks.remove(subtask.getTaskID());
                }
            }
        }
    }
    void removeSubtaskByID(int subtaskID){
        if (subtasks.containsKey(subtaskID)){
            int epicID = subtasks.get(subtaskID).getEpicID();
            for (int i=0;i<epics.get(epicID).mySubtasksID.size();i++){
                if (epics.get(epicID).mySubtasksID.get(i)==subtaskID){
                    epics.get(epicID).mySubtasksID.remove(i);
                }
            }
            subtasks.remove(subtaskID);
            checkStatusOfEpic(epics.get(epicID));
        }
    }
    void removeAllTasks(){
        ArrayList<Integer> listForRemoval = new ArrayList<>();
            for (Integer taskID:tasks.keySet()) {
                listForRemoval.add(taskID);
        }
        for (Integer taskID:listForRemoval){
            removeTaskByID(taskID);
        }
    }

    void removeAllEpics(){
        ArrayList<Integer> listForRemoval = new ArrayList<>();
        for (Integer taskID:epics.keySet()) {
            listForRemoval.add(taskID);
        }
        for (Integer taskID:listForRemoval){
            removeEpicByID(taskID);
        }
    }
    void removeAllSubtasks(){
        ArrayList<Integer> listForRemoval = new ArrayList<>();
        for (Integer taskID:subtasks.keySet()) {
            listForRemoval.add(taskID);
        }
        for (Integer taskID:listForRemoval){
            removeSubtaskByID(taskID);
        }
    }
    void updateTask(Task task,int taskID){
        task.setTaskID(taskID);
        if(tasks.containsKey(taskID)){
            tasks.put(task.getTaskID(),task);
        }
    }
    void updateSubtask(Subtask subtask,int subtaskID){
        subtask.setTaskID(subtaskID);
        if(subtasks.containsKey(subtask.getTaskID())){
            subtasks.put(subtask.getTaskID(),subtask);
            checkStatusOfEpic(epics.get(subtask.getEpicID()));
        }
    }
    void updateEpic(Epic epic, int epicID){
        epic.setTaskID(epicID);
        if(epics.containsKey(epic.getTaskID())){
            for (Integer subtaskID:epics.get(epic.getTaskID()).mySubtasksID) {
                epic.mySubtasksID.add(subtaskID);
            }
            epics.put(epic.getTaskID(),epic);
            checkStatusOfEpic(epic);
        }
    }

    Object returnTaskByID(int taskID){
        return tasks.getOrDefault(taskID, null);
    }
    Object returnEpicByID(int taskID){
        return epics.getOrDefault(taskID, null);
    }
    Object returnSubtaskByID(int taskID){
        return subtasks.getOrDefault(taskID, null);
    }
    void printTaskByID(int taskID){
        if(tasks.containsKey(taskID)){
            System.out.println(returnTaskByID(taskID).toString());
        }
    }
    void printEpicByID(int taskID){
        if(epics.containsKey(taskID)){
            System.out.println(returnEpicByID(taskID).toString());
        }
    }
    void printSubtaskByID(int subtaskID){
        if(subtasks.containsKey(subtaskID)){
            System.out.println(returnSubtaskByID(subtaskID).toString());
        }
    }


    int generateID(){
        int newID = rawID;
        rawID++;
        return newID;
    }
}
