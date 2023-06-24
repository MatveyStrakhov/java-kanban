package manager;

import java.io.IOException;

public class Managers {
  public static TaskManager getDefault(){
      //return new FileBackedTasksManager("resources/lastSessionSaved.csv");
      try {return new HttpTaskManager("http://localhost:8078");}
      catch (IOException|InterruptedException e){
          System.out.println("error while starting server");
          return null;
      }
  }
  public static HistoryManager getDefaultHistory(){
      return new InMemoryHistoryManager();
  }
}
