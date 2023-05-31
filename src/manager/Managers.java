package manager;

public class Managers {
  public static TaskManager getDefault(){
      return new FileBackedTasksManager("src/lastSessionSaved.csv");
  }
  public static HistoryManager getDefaultHistory(){
      return new InMemoryHistoryManager();
  }
}
