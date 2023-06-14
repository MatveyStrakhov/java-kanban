package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private int rawID = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected TreeMap<LocalDateTime,Task> sortedTasks = new TreeMap<>(); //sorted automatically
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addNewTask(Task task) {
        task.setTaskID(generateID());
        tasks.put(task.getTaskID(), task);
    }

    @Override
    public void addNewSubtask(Subtask subtask, int epicID) {
        if (epics.containsKey(epicID)) {
            subtask.setTaskID(generateID());
            subtask.setTaskID(subtask.getTaskID());
            subtasks.put(subtask.getTaskID(), subtask);
            epics.get(epicID).getMySubtasksID().add(subtask.getTaskID());
            checkStatusOfEpic(epics.get(epicID));
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setTaskID(generateID());
        epics.put(epic.getTaskID(), epic);
    }

    @Override
    public void checkStatusOfEpic(Epic epic) {
        boolean newTask = false;
        boolean done = false;
        if(epic.getStartTime(subtasks).isPresent()){epic.setStartTime(epic.getStartTime(subtasks).get());}
        epic.setDuration(epic.getDuration(subtasks));
        if(epic.getEndTime()!=null){epic.setEndTime(epic.getEndTime());}
        if (!epic.getMySubtasksID().isEmpty()) {
            for (Integer subtaskID : epic.getMySubtasksID()) {
                if (subtasks.get(subtaskID).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                    epic.setStatus(TaskStatus.IN_PROGRESS);
                    return;
                } else if (subtasks.get(subtaskID).getStatus().equals(TaskStatus.DONE)) {
                    done = true;
                } else if (subtasks.get(subtaskID).getStatus().equals(TaskStatus.NEW)) {
                    newTask = true;
                }


                if (done && !newTask) {
                    epic.setStatus(TaskStatus.DONE);
                } else {
                    epic.setStatus(TaskStatus.NEW);
                }
            }
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public void printAllTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task.toString());
        }
    }

    @Override
    public void printAllEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic.toString());
        }
    }

    @Override
    public void printAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask.toString());
        }
    }

    @Override
    public void printSubtasksByEpicID(int epicID) {
        for (Integer subtaskID : epics.get(epicID).getMySubtasksID()) {
            System.out.println(subtasks.get(subtaskID).toString());
        }
    }

    @Override
    public void removeTaskByID(int taskID) {
        if (tasks.containsKey(taskID)) {
            tasks.remove(taskID);
            historyManager.historyRemove(taskID);
        }
    }

    @Override
    public void removeEpicByID(int epicID) {
        if (epics.containsKey(epicID)) {
            epics.remove(epicID);
            historyManager.historyRemove(epicID);
            List<Integer> idForRemoval = new ArrayList<>();
            for (Subtask subtask : subtasks.values()) {
                if (subtask.getEpicID() == epicID) {
                    idForRemoval.add(subtask.getTaskID());
                    historyManager.historyRemove(subtask.getTaskID());
                }
            }
            for (int id : idForRemoval) {
                subtasks.remove(id);
            }
            idForRemoval.clear();
        }
    }

    @Override
    public void removeSubtaskByID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            int epicID = subtasks.get(subtaskID).getEpicID();
            for (int i = 0; i < epics.get(epicID).getMySubtasksID().size(); i++) {
                if (epics.get(epicID).getMySubtasksID().get(i) == subtaskID) {
                    epics.get(epicID).removeElementFromMySubtasksID(i);
                }
            }
            subtasks.remove(subtaskID);
            historyManager.historyRemove(subtaskID);
            checkStatusOfEpic(epics.get(epicID));
        }
    }

    @Override
    public void removeAllTasks() {
        for(Integer taskId :tasks.keySet()){
            historyManager.historyRemove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for(Integer taskId :subtasks.keySet()){
            historyManager.historyRemove(taskId);
        }
        for(Integer taskId :epics.keySet()){
            historyManager.historyRemove(taskId);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for(Integer taskId :subtasks.keySet()){
            historyManager.historyRemove(taskId);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearMySubtaskID();
            checkStatusOfEpic(epic);
        }
    }

    @Override
    public void updateTask(Task task, int taskID) {
        task.setTaskID(taskID);
        if (tasks.containsKey(taskID)) {
            tasks.put(task.getTaskID(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskID) {
        subtask.setTaskID(subtaskID);
        if (subtasks.containsKey(subtask.getTaskID())) {
            subtasks.put(subtask.getTaskID(), subtask);
            checkStatusOfEpic(epics.get(subtask.getEpicID()));
        }
    }

    @Override
    public void updateEpic(Epic epic, int epicID) {
        epic.setTaskID(epicID);
        if (epics.containsKey(epic.getTaskID())) {
            for (Integer subtaskID : epics.get(epic.getTaskID()).getMySubtasksID()) {
                epic.getMySubtasksID().add(subtaskID);
            }
            epics.put(epic.getTaskID(), epic);
            checkStatusOfEpic(epic);
        }
    }

    @Override
    public Task returnTaskByID(int taskID) {
        if (tasks.containsKey(taskID)) {

            historyManager.historyAdd(tasks.get(taskID));
            return tasks.get(taskID);
        } else {
            return null;
        }
    }

    @Override
    public Epic returnEpicByID(int taskID) {
        if (epics.containsKey(taskID)) {
            historyManager.historyAdd(epics.get(taskID));
            return epics.get(taskID);
        } else {
            return null;
        }
    }

    @Override
    public Subtask returnSubtaskByID(int taskID) {
        if (subtasks.containsKey(taskID)) {
            historyManager.historyAdd(subtasks.get(taskID));
            return subtasks.get(taskID);
        } else {
            return null;
        }
    }

    @Override
    public void printTaskByID(int taskID) {
        if (tasks.containsKey(taskID)) {
            System.out.println(returnTaskByID(taskID).toString());
        }
    }

    @Override
    public void printEpicByID(int taskID) {
        if (epics.containsKey(taskID)) {
            System.out.println(returnEpicByID(taskID).toString());
        }
    }

    @Override
    public void printSubtaskByID(int subtaskID) {
        if (subtasks.containsKey(subtaskID)) {
            System.out.println(returnSubtaskByID(subtaskID).toString());
        }
    }

    @Override
    public void printHistory() {
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.toString());
        }
    }


    private int generateID() {
        return rawID++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> returnAllTasks() {
        if (tasks.isEmpty()){
            return Collections.emptyList();
        }
        else{
        return new ArrayList<>(tasks.values());}
    }
    @Override
    public List<Task> returnAllEpics() {
        if (epics.isEmpty()){
            return Collections.emptyList();
        }
        else{
            return new ArrayList<>(epics.values());}
    }
    @Override
    public List<Task> returnAllSubtasks() {
        if (subtasks.isEmpty()){
            return Collections.emptyList();
        }
        else{
            return new ArrayList<>(subtasks.values());}
    }

}
