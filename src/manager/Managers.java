package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.LocalDateTimeAdapter;
import java.time.LocalDateTime;


public class Managers {
    private static final LocalDateTimeAdapter adapter = new LocalDateTimeAdapter();
  public static TaskManager getDefault(){
      return new HttpTaskManager("http://localhost:8078");
  }
  public static Gson getDefaultGson(){
      return new GsonBuilder().serializeNulls()
              .registerTypeAdapter(LocalDateTime.class, adapter.nullSafe())
              .setPrettyPrinting().create();
  }
  public static HistoryManager getDefaultHistory(){
      return new InMemoryHistoryManager();
  }
}
