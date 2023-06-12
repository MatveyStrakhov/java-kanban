package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    @BeforeEach
    void beforeEachTest(){
        taskManager = new FileBackedTasksManager("src/lastSessionSavedTest.csv");
    }

}