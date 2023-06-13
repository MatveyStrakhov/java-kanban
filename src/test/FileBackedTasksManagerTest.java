package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static manager.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    @BeforeEach
    void beforeEachTest(){
        taskManager = new FileBackedTasksManager("src/test/lastSessionSavedTest.csv");
    }
    @Test
    void shouldLoadNoTasksFromEmptyFile(){
        FileBackedTasksManager emptyTaskManager = loadFromFile("src/test/EmptyLastSessionSavedTest.csv");
        assertEquals(Collections.emptyList(),emptyTaskManager.returnAllTasks());
        assertEquals(Collections.emptyList(),emptyTaskManager.returnAllSubtasks());
        assertEquals(Collections.emptyList(),emptyTaskManager.returnAllEpics());
        assertEquals(Collections.emptyList(),emptyTaskManager.getHistory());
    }
    @Test
    void shouldLoadEpicAndHistoryFromPrefilledFile(){
        FileBackedTasksManager prefilledTaskManager = loadFromFile("src/test/prefilledLastSessionSavedTest.csv");
        assertEquals(Collections.emptyList(),prefilledTaskManager.returnAllTasks());
        assertEquals(Collections.emptyList(),prefilledTaskManager.returnAllSubtasks());
        assertNotEquals(Collections.emptyList(),prefilledTaskManager.returnAllEpics());
        assertEquals(1,prefilledTaskManager.getHistory().size());
    }

}