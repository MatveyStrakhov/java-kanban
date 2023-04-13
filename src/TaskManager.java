import java.util.List;

interface TaskManager {
    void addNewTask(Task task);

    void addNewSubtask(Subtask subtask, int epicID);

    void addNewEpic(Epic epic);

    void checkStatusOfEpic(Epic epic);

    void printAllTasks();

    void printAllEpics();

    void printAllSubtasks();

    void printSubtasksByEpicID(int epicID);

    void removeTaskByID(int taskID);

    void removeEpicByID(int epicID);

    void removeSubtaskByID(int subtaskID);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void updateTask(Task task, int taskID);

    void updateSubtask(Subtask subtask, int subtaskID);

    void updateEpic(Epic epic, int epicID);

    Task returnTaskByID(int taskID);
    Epic returnEpicByID(int taskID);
    Subtask returnSubtaskByID(int taskID);

    void printTaskByID(int taskID);

    void printEpicByID(int taskID);

    void printSubtaskByID(int subtaskID);
    void printHistory();
    int generateID();
}
