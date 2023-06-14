package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    void addNewTask(Task task); //done

    void addNewSubtask(Subtask subtask, int epicID); //done

    void addNewEpic(Epic epic); //done

    void checkStatusOfEpic(Epic epic);

    void printAllTasks(); //done

    void printAllEpics(); //done

    void printAllSubtasks(); //done
    void printSubtasksByEpicID(int epicID); //done

    void removeTaskByID(int taskID); //done

    void removeEpicByID(int epicID); //done

    void removeSubtaskByID(int subtaskID); //done

    void removeAllTasks();//done

    void removeAllEpics();//done

    void removeAllSubtasks();//done

    void updateTask(Task task, int taskID); //done

    void updateSubtask(Subtask subtask, int subtaskID);  //done

    void updateEpic(Epic epic, int epicID); //done

    Task returnTaskByID(int taskID); //done
    Epic returnEpicByID(int taskID); //done
    Subtask returnSubtaskByID(int taskID); //done

    void printTaskByID(int taskID); //done

    void printEpicByID(int taskID); //done

    void printSubtaskByID(int subtaskID); //done
    void printHistory(); //done

    List<Task> getHistory(); //done

    List<Task> returnAllTasks(); //done

    List<Task> returnAllEpics(); //done

    List<Task> returnAllSubtasks(); //done
    List<Task> getPrioritizedTasks();
}
