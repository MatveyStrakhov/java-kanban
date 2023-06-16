package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest{
    InMemoryHistoryManager historyManager;
    Task createNewTask() {
        return new Task("Test", "Test description", TaskStatus.NEW);
    }
    @BeforeEach
    void beforeEach(){
        this.historyManager = new InMemoryHistoryManager();
    }
    @Test
    void shouldAddOneElementToHistory() {
        Task task = createNewTask();
        historyManager.historyAdd(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
    @Test
    void shouldAddNewTaskToHistoryAndReturnIt(){
        Task task1 = createNewTask();
        historyManager.historyAdd(task1);
        List<Task> list = Collections.singletonList(task1);
        assertEquals(list,historyManager.getHistory());
    }
    @Test
    void shouldReturnOnlyOneTaskIfTasksAreDuplicated(){
        Task task1 = createNewTask();
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task1);
        List<Task> list = Collections.singletonList(task1);
        assertEquals(list,historyManager.getHistory());
    }
    @Test
    void shouldReturnNothingIfHistoryIsEmpty(){
        List<Task> list = Collections.emptyList();
        assertEquals(list,historyManager.getHistory());
    }
    @Test
    void shouldDeleteFirstTask(){
        Task task0 = createNewTask();
        task0.setTaskID(0);
        Task task1 = createNewTask();
        task1.setTaskID(1);
        Task task2 = createNewTask();
        task2.setTaskID(2);
        historyManager.historyAdd(task0);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyRemove(task0.getTaskID());
        List<Task> list = new ArrayList<>();
        list.add(task1);
        list.add(task2);
        assertEquals(list,historyManager.getHistory());

    }
    @Test
    void shouldDeleteMiddleTask(){
        Task task0 = createNewTask();
        task0.setTaskID(0);
        Task task1 = createNewTask();
        task1.setTaskID(1);
        Task task2 = createNewTask();
        task2.setTaskID(2);
        historyManager.historyAdd(task0);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyRemove(task1.getTaskID());
        List<Task> list = new ArrayList<>();
        list.add(task0);
        list.add(task2);
        assertEquals(list,historyManager.getHistory());

    }
    @Test
    void shouldDeleteLastTask(){
        Task task0 = createNewTask();
        task0.setTaskID(0);
        Task task1 = createNewTask();
        task1.setTaskID(1);
        Task task2 = createNewTask();
        task2.setTaskID(2);
        historyManager.historyAdd(task0);
        historyManager.historyAdd(task1);
        historyManager.historyAdd(task2);
        historyManager.historyRemove(task2.getTaskID());
        List<Task> list = new ArrayList<>();
        list.add(task0);
        list.add(task1);
        assertEquals(list,historyManager.getHistory());

    }



}
