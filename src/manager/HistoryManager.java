package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void historyAdd(Task task);
    List<Task> getHistory();
}
